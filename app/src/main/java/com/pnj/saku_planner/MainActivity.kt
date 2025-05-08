package com.pnj.saku_planner

import android.app.Application
import com.pnj.saku_planner.core.ui.theme.SakuPlannerTheme
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SakuPlannerApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SakuPlannerTheme {
                KakeiboApp()
            }
        }
    }
}
