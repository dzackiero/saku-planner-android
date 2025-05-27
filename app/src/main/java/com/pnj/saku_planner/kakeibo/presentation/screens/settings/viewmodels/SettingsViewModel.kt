package com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.pnj.saku_planner.core.database.DatabaseSeeder
import com.pnj.saku_planner.core.sync.AlarmSchedulerUtil
import com.pnj.saku_planner.core.sync.SyncScheduler
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val settingsDataStore: SettingsDataStore,
    private val databaseSeeder: DatabaseSeeder,
) : ViewModel() {

    private val workManager = WorkManager.getInstance(appContext)
    val manualSyncWorkInfoState: StateFlow<WorkInfo?> =
        workManager.getWorkInfosForUniqueWorkLiveData(SyncScheduler.MANUAL_SYNC_WORK_NAME) // Use the unique name
            .asFlow()
            .mapNotNull { workInfoList ->
                workInfoList.firstOrNull()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    fun onSyncNowClicked() {
        Timber.d("SettingsViewModel: Sync Now button clicked.")
        SyncScheduler.triggerManualSync(appContext)
    }

    fun logout() {
        viewModelScope.launch {
            AlarmSchedulerUtil.cancelDailySync(appContext)
            settingsDataStore.clearUserSession()
        }
    }

    fun resetDatabase() {
        viewModelScope.launch {
            databaseSeeder.resetDatabase()
        }
    }
}