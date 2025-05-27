package com.pnj.saku_planner.core.sync // Or your chosen package

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import timber.log.Timber
import java.util.Calendar
import java.util.Locale

object AlarmSchedulerUtil {

    private const val DAILY_SYNC_ALARM_REQUEST_CODE = 12345

    fun scheduleDailySync(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, SyncAlarmReceiver::class.java).apply {
            action = "com.pnj.saku_planner.action.DAILY_SYNC_ALARM" // Unique action for clarity
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            DAILY_SYNC_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // If the calculated time for today is in the past, schedule for the same time tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

        try {
            // Check if the app can schedule exact alarms (Android S / API 31+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                Timber.w("Cannot schedule exact alarms. User permission likely not granted or app doesn't qualify. Falling back to inexact repeating alarm for daily sync around $formattedTime.")
                // Fallback to an inexact repeating alarm. This alarm will repeat daily automatically.
                // WorkManager's SyncWorker will still run, but the trigger time is less precise.
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY, // Repeats daily
                    pendingIntent
                )
                Timber.i("Inexact daily sync alarm scheduled for approximately $formattedTime. Next approximate run: ${calendar.time}")
            } else {
                // Permission is granted, or on an OS version < Android S where it's not restricted as much,
                // or on Android S+ where permission is granted.
                // This is a ONE-TIME exact alarm. The SyncWorker MUST reschedule it for the next day.
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
                Timber.i("Exact daily sync alarm scheduled for $formattedTime. Next run at: ${calendar.time}")
            }
        } catch (se: SecurityException) {
            // This catch block is a further safety net.
            Timber.e(
                se,
                "SecurityException during alarm scheduling despite checks. Attempting inexact repeating fallback."
            )
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            Timber.i("Inexact daily sync alarm (fallback from SE) scheduled for approximately $formattedTime. Next approximate run: ${calendar.time}")
        }
    }

    fun cancelDailySync(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context.applicationContext, SyncAlarmReceiver::class.java).apply {
            action = "com.pnj.saku_planner.action.DAILY_SYNC_ALARM"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            DAILY_SYNC_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Timber.i("Daily sync alarm cancelled.")
        } else {
            Timber.i("Daily sync alarm was not previously set (or already cancelled), no need to cancel again.")
        }
    }
}