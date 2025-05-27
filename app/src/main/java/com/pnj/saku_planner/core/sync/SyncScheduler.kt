package com.pnj.saku_planner.core.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import timber.log.Timber

object SyncScheduler {

    const val MANUAL_SYNC_WORK_NAME = "ManualSakuPlannerDataSync"

    private const val PERIODIC_SYNC_WORK_NAME = "PeriodicSakuPlannerDataSync"
    private const val SYNC_WORK_TAG = "SakuPlannerSyncTag"

    /**
     * Schedules a periodic sync to run roughly every 6 hours.
     * It will only run when the network is connected.
     * Uses ExistingPeriodicWorkPolicy.KEEP to avoid rescheduling if already active.
     */
    fun schedulePeriodicSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSyncRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(12, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(SYNC_WORK_TAG)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PERIODIC_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncRequest
        )
        Timber.d("Periodic sync work scheduled: runs every 6 hours when connected.")
    }

    /**
     * Triggers an immediate (one-time) sync request.
     * It will only run when the network is connected.
     * Uses ExistingWorkPolicy.REPLACE to ensure only one manual sync runs or is pending.
     */
    fun triggerManualSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val manualSyncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            .addTag(SYNC_WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            MANUAL_SYNC_WORK_NAME,
            ExistingWorkPolicy.REPLACE, // If user taps multiple times, replace the old request
            manualSyncRequest
        )
        Timber.d("Manual sync work triggered.")
    }

    /**
     * Optional: Cancel all sync work.
     */
    fun cancelAllSyncWork(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(SYNC_WORK_TAG)
        Timber.d("All sync work cancelled.")
    }
}