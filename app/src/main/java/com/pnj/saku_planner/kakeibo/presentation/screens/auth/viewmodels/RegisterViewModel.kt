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
class RegisterViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val settingsDataStore: SettingsDataStore,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state

    val callback = RegisterCallback(
        onNameChange = {
            _state.value = _state.value.copy(name = it, errorMessage = null)
        },
        onEmailChange = {
            _state.value = _state.value.copy(email = it, errorMessage = null)
        },
        onPasswordChange = {
            _state.value = _state.value.copy(password = it, errorMessage = null)
        },
        onPasswordConfirmChange = {
            _state.value = _state.value.copy(passwordConfirm = it, errorMessage = null)
        }
    )

    fun register() {
        val name = _state.value.name
        val email = _state.value.email
        val password = _state.value.password
        val passwordConfirm = _state.value.passwordConfirm
        viewModelScope.launch {
            authRepository.register(name, email, password, passwordConfirm).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false, user = resource.data) }
                        // Assuming resource.data != null means successful registration & login,
                        // and token is saved by AuthRepository
                        if (resource.data != null) {
                            // Schedule the daily sync
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

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: AuthResponse? = null
)

data class RegisterCallback(
    val onNameChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val onPasswordChange: (String) -> Unit = {},
    val onPasswordConfirmChange: (String) -> Unit = {}
)
