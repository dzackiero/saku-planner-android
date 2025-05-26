package com.pnj.saku_planner

import android.app.Application
import com.pnj.saku_planner.core.theme.KakeiboTheme
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.pnj.saku_planner.core.sync.SyncScheduler
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SakuPlannerApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        SyncScheduler.schedulePeriodicSync(this)
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            KakeiboTheme {
                KakeiboApp()
            }
        }
    }
}
