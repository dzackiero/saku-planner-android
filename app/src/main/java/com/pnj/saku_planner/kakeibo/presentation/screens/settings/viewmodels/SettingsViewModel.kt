package com.pnj.saku_planner.kakeibo.presentation.screens.settings.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.DatabaseSeeder
import com.pnj.saku_planner.kakeibo.data.local.UserStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userStorage: UserStorage,
    private val databaseSeeder: DatabaseSeeder,
) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            userStorage.clear()
        }
    }

    fun resetDatabase() {
        viewModelScope.launch {
            databaseSeeder.resetDatabase()
        }
    }
}