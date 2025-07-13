package com.pnj.saku_planner.kakeibo.presentation.screens.auth.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.sync.AlarmSchedulerUtil
import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import com.pnj.saku_planner.kakeibo.data.local.UserPreferencesKeys
import com.pnj.saku_planner.kakeibo.data.remote.dto.AuthResponse
import com.pnj.saku_planner.kakeibo.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val authRepository: AuthRepository,
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    val callback = LoginCallback(
        onEmailChange = {
            _state.value = _state.value.copy(email = it, errorMessage = null)
        },
        onPasswordChange = {
            _state.value = _state.value.copy(password = it, errorMessage = null)
        },
    )

    fun login() {
        val email = _state.value.email
        val password = _state.value.password
        viewModelScope.launch {
            authRepository.login(email, password).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false, user = resource.data) }
                        if (resource.data != null) {
                            val syncTimePair = settingsDataStore.syncTimeFlow.firstOrNull() ?: Pair(
                                UserPreferencesKeys.DEFAULT_SYNC_HOUR,
                                UserPreferencesKeys.DEFAULT_SYNC_MINUTE
                            )
                            AlarmSchedulerUtil.scheduleDailySync(
                                appContext,
                                syncTimePair.first,
                                syncTimePair.second
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                }
            }
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: AuthResponse? = null
)

data class LoginCallback(
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
)

