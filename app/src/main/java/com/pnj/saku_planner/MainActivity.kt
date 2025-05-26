package com.pnj.saku_planner

import android.app.Application
import com.pnj.saku_planner.core.theme.KakeiboTheme
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SakuPlannerApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())
        Timber.d("Timber initialized!")

        enableEdgeToEdge()
        setContent {
            KakeiboTheme {
                KakeiboApp()
            }
        }
    }
}
