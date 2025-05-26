package com.pnj.saku_planner.core.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SyncAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action == "com.pnj.saku_planner.action.DAILY_SYNC_ALARM") {
            Timber.d("SyncAlarmReceiver: Daily alarm received. Triggering sync worker.")
            SyncScheduler.triggerManualSync(context.applicationContext)
        }
    }
}