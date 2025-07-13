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
import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import com.pnj.saku_planner.kakeibo.domain.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    val settingsDataStore: SettingsDataStore,
    private val databaseSeeder: DatabaseSeeder,
    private val dataRepository: DataRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state
    val isOfflineModeFlow = settingsDataStore.isOfflineModeFlow

    private val workManager = WorkManager.getInstance(appContext)
    val manualSyncWorkInfoState: StateFlow<WorkInfo?> =
        workManager.getWorkInfosForUniqueWorkLiveData(SyncScheduler.MANUAL_SYNC_WORK_NAME)
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
        SyncScheduler.triggerManualSync(appContext)
    }

    fun loadSyncData() {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.loadDataFromServer().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            }
        }
    }

    fun disableOfflineMode() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDataStore.setOfflineMode(false)
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseSeeder.clearDatabase()
            AlarmSchedulerUtil.cancelDailySync(appContext)
            settingsDataStore.clearUserSession()
        }
    }

    fun resetDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseSeeder.resetAndSeedDatabase()
        }
    }
}

data class SettingsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)