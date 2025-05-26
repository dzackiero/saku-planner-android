package com.pnj.saku_planner.core.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import com.pnj.saku_planner.kakeibo.data.local.UserPreferencesKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && context != null) {
            scope.launch {
                if (settingsDataStore.isLoggedInBlocking()) {
                    Timber.d("BootCompletedReceiver: Device booted & user logged in. Rescheduling daily sync alarm.")
                    val syncTime = settingsDataStore.syncTimeFlow.firstOrNull() ?: Pair(
                        UserPreferencesKeys.DEFAULT_SYNC_HOUR,
                        UserPreferencesKeys.DEFAULT_SYNC_MINUTE
                    )
                    AlarmSchedulerUtil.scheduleDailySync(
                        context.applicationContext,
                        syncTime.first,
                        syncTime.second
                    )
                } else {
                    Timber.i("BootCompletedReceiver: Device booted, but no user logged in. Sync alarm not scheduled.")
                }
            }
        }
    }
}