package com.pnj.saku_planner.kakeibo.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleSettingsViewModel @Inject constructor(
    private val dataStore: SettingsDataStore
) : ViewModel() {

    val state = dataStore.settings

    fun setDailySync(isEnable: Boolean) {
        viewModelScope.launch {
            dataStore.setDailySync(isEnable)
        }
    }

    fun setReflectionNotification(isEnable: Boolean) {
        viewModelScope.launch {
            dataStore.setMonthReflectionNotification(isEnable)
        }
    }

    fun setReflectionDate(date: Int) {
        viewModelScope.launch {
            dataStore.setMonthReflectionDate(date)
        }
    }

}

data class ScheduleSettingsState(
    val isDailySync: Boolean = false,
    val isReflectionNotification: Boolean = false,
    val reflectionNotificationDate: Int = 1,
)