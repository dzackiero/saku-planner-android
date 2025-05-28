package com.pnj.saku_planner.kakeibo.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.local.SettingsDataStore
import com.pnj.saku_planner.kakeibo.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _state: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    val callback = ProfileCallback(
        onNameChange = {
            _state.value = _state.value.copy(name = it, errorMessage = null)
        },
        onEmailChange = {
            _state.value = _state.value.copy(email = it, errorMessage = null)
        },
        updateProfile = { updateProfile() }
    )

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            settingsDataStore.session.collect { profile ->
                _state.value = _state.value.copy(
                    name = profile.name ?: "",
                    email = profile.email ?: "",
                )
            }
        }
    }

    private fun updateProfile() {
        val stateValue = _state.value
        viewModelScope.launch {
            authRepository.update(
                name = stateValue.name,
                email = stateValue.email
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false, errorMessage = null) }
                        loadUserProfile()
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

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class ProfileCallback(
    val onNameChange: (String) -> Unit = {},
    val onEmailChange: (String) -> Unit = {},
    val updateProfile: () -> Unit = {}
)
