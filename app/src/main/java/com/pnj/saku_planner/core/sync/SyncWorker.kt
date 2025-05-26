package com.pnj.saku_planner.core.sync


import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import com.pnj.saku_planner.kakeibo.data.local.UserPreferencesKeys
import com.pnj.saku_planner.kakeibo.domain.repository.DataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import timber.log.Timber

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: DataRepository,
    private val settingsDataStore: SettingsDataStore,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Timber.d("SyncWorker: Starting data upload...")

        // Check if user is logged in
        if (!settingsDataStore.isLoggedInBlocking()) { // Or observe tokenFlow.firstOrNull()
            Timber.i("SyncWorker: No user logged in. Skipping sync. Work considered successful (no action needed).")
            return Result.success()
        }


        val finalResource = try {
            // Get the last emission that isn't Loading, or null if no such emission occurs
            repository.uploadDataToServer()
                .filter { it !is Resource.Loading }
                .lastOrNull()
        } catch (e: Exception) {
            Timber.e(e, "SyncWorker: Exception during uploadDataToServer flow collection.")
            Resource.Error("Sync failed due to an unexpected error in the repository flow: ${e.message}")
        }

        val workResult = when (finalResource) {
            is Resource.Success -> {
                Timber.d("SyncWorker: Data upload successful. Message: ${finalResource.data}")
                Result.success()
            }

            is Resource.Error -> {
                Timber.w("SyncWorker: Data upload failed. Message: ${finalResource.message}. Retrying...")
                Result.failure()
            }

            is Resource.Loading -> {
                Timber.w("SyncWorker: Data upload is still loading. This should not happen in doWork.")
                Result.retry()
            }

            null -> {
                Timber.w("SyncWorker: Sync flow completed without a definitive Success/Error result. Assuming retry.")
                Result.retry()
            }
        }

        // --- Reschedule the ALARM for the NEXT DAY ---
        // This happens regardless of the current sync's success/failure,
        // because this worker instance is for *today's* scheduled attempt.
        // We always want an alarm for *tomorrow's* attempt.
        // (The login check at the start of doWork() will prevent sync if user logs out by then)
        try {
            Timber.d("SyncWorker: Attempting to reschedule daily alarm for the next occurrence.")
            val syncTimePair = settingsDataStore.syncTimeFlow.firstOrNull() ?: Pair(
                UserPreferencesKeys.DEFAULT_SYNC_HOUR,
                UserPreferencesKeys.DEFAULT_SYNC_MINUTE
            )
            AlarmSchedulerUtil.scheduleDailySync(
                appContext,
                syncTimePair.first,
                syncTimePair.second
            )
        } catch (e: Exception) {
            Timber.e(
                e,
                "SyncWorker: CRITICAL - Failed to reschedule daily alarm after work completion."
            )
            // If this rescheduling fails, future daily automatic syncs might not happen until
            // the app is restarted or the user logs in again.
            // The current workResult (success/retry) for *this* sync attempt should still be honored.
        }

        return workResult
    }
}