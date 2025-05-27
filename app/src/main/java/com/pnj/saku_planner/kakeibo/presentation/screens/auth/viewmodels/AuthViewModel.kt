package com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
            // Start collecting the token Flow from UserStorage.
            // receive updates whenever the token changes in DataStore.
            settingsDataStore.token.collect { token ->
                Timber.tag("AuthViewModel").d("Token received: $token")
                // Check if the received token is null or empty.
                if (token.isNullOrEmpty()) {
                    // If no token, set state to LOGGED_OUT (if it's not already).
                    if (_userState.value != UserState.LOGGED_OUT) {
                        _userState.value = UserState.LOGGED_OUT
                    }
                } else {
                    // If a token exists, set state to LOGGED_IN (if it's not already).
                    if (_userState.value != UserState.LOGGED_IN) {
                        _userState.value = UserState.LOGGED_IN
                    }
                }
            }
        }
    }
}