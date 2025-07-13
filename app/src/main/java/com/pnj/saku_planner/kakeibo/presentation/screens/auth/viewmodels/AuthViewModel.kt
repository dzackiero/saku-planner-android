package com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject

enum class UserState {
    LOGGED_IN,
    LOGGED_OUT,
    UNKNOWN
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _userState = MutableStateFlow(UserState.UNKNOWN)
    val userState: StateFlow<UserState> = _userState

    init {
        viewModelScope.launch {
            // Combine both token and offline mode flows to determine the login state
            settingsDataStore.token.combine(settingsDataStore.isOfflineModeFlow) { token, isOfflineMode ->
                Timber.tag("AuthViewModel").d("Token: $token, Offline Mode: $isOfflineMode")
                when {
                    // If offline mode is enabled, consider the user as logged in
                    isOfflineMode -> UserState.LOGGED_IN
                    // If token exists, user is logged in normally
                    !token.isNullOrEmpty() -> UserState.LOGGED_IN
                    // Otherwise, user is logged out
                    else -> UserState.LOGGED_OUT
                }
            }.collect { state ->
                if (_userState.value != state) {
                    _userState.value = state
                }
            }
        }
    }
}