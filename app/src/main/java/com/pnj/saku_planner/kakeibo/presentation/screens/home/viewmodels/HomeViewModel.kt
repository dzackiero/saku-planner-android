package com.pnj.saku_planner.kakeibo.presentation.screens.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.DatabaseSeeder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseSeeder: DatabaseSeeder,
) : ViewModel() {
    fun resetDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseSeeder.resetDatabase()
        }
    }
}