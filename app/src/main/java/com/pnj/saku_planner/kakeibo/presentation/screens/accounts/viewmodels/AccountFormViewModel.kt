package com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.TargetEntity
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.core.util.validateRequired
import com.pnj.saku_planner.kakeibo.domain.enum.AccountType
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountFormViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
) : ViewModel() {
    private var _formState = MutableStateFlow(AccountFormState())
    val formState: StateFlow<AccountFormState> = _formState

    val callbacks = AccountFormCallback(
        onAccountNameChange = {
            _formState.value = _formState.value.copy(accountName = it)
        },
        onDescriptionChange = {
            _formState.value = _formState.value.copy(description = it)
        },
        onCurrentBalanceChange = {
            _formState.value = _formState.value.copy(currentBalance = it)
        },
        onAccountTypeChange = {
            _formState.value = _formState.value.copy(
                accountType = it,
                targetAmount = if (it == AccountType.Savings) _formState.value.targetAmount else null,
                targetDuration = if (it == AccountType.Savings) _formState.value.targetDuration else null,
                targetStartDate = if (it == AccountType.Savings) _formState.value.targetStartDate else null,
            )
        },
        onTargetAmountChange = {
            _formState.value = _formState.value.copy(targetAmount = it)
        },
        onTargetDurationChange = {
            _formState.value = _formState.value.copy(targetDuration = it)
        },
        onTargetStartDateChange = {
            _formState.value = _formState.value.copy(targetStartDate = it)
        },
        onSubmit = { onSuccess ->
            if (submit()) {
                onSuccess()
            }
        }
    )

    fun loadAccount(accountId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = accountRepository.getAccountById(accountId)?.toUi() ?: return@launch

            _formState.value = _formState.value.copy(
                accountId = account.id,
                accountName = account.name,
                currentBalance = account.balance,
                description = account.description ?: "",
            )
        }
    }

    fun deleteAccount() {
        val accountId = _formState.value.accountId ?: return
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.deleteAccount(accountId)
        }
    }

    private fun submit(): Boolean {
        if (validateForm()) return false

        viewModelScope.launch(Dispatchers.IO) {
            val values = _formState.value
            val accountEntity = AccountEntity(
                id = values.accountId ?: 0,
                name = values.accountName,
                balance = values.currentBalance ?: 0.0,
                description = values.description,
            )

            var targetEntity: TargetEntity? = null
            if (values.accountType == AccountType.Savings) {
                targetEntity = TargetEntity(
                    id = 0,
                    duration = values.targetDuration ?: 0,
                    startDate = values.targetStartDate ?: 0,
                    targetAmount = values.targetAmount ?: 0.0,
                )
            }

            accountRepository.saveAccount(accountEntity, target = targetEntity)
        }

        return true
    }

    private fun validateForm(): Boolean {
        val formValues = _formState.value

        _formState.value = _formState.value.copy(
            accountNameError = validateRequired(formValues.accountName)
        )
        _formState.value = _formState.value.copy(
            currentBalanceError = validateRequired(formValues.currentBalance)
        )

        if (formValues.accountType == AccountType.Savings) {
            _formState.value = _formState.value.copy(
                targetAmountError = validateRequired(formValues.targetAmount)
            )

            _formState.value = _formState.value.copy(
                targetDurationError = validateRequired(formValues.targetDuration)
            )
        }

        return _formState.value.hasError()
    }
}

data class AccountFormState(
    val accountId: Int? = null,

    val accountName: String = "",
    val accountNameError: String? = null,

    val currentBalance: Double? = null,
    val currentBalanceError: String? = null,

    val accountType: AccountType = AccountType.Checking,
    val accountTypeError: String? = null,

    val targetAmount: Double? = null,
    val targetAmountError: String? = null,

    val targetDuration: Int? = null,
    val targetDurationError: String? = null,

    val targetStartDate: Long? = null,
    val targetStartDateError: String? = null,

    val description: String = "",
    val descriptionError: String? = null,
)


fun AccountFormState.hasError(): Boolean {
    return listOf(
        accountNameError,
        currentBalanceError,
        descriptionError,
        targetAmountError,
        targetDurationError,
        targetStartDateError,
    ).any { !it.isNullOrBlank() }
}


data class AccountFormCallback(
    val onAccountNameChange: (String) -> Unit,
    val onCurrentBalanceChange: (Double?) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onTargetAmountChange: (Double?) -> Unit,
    val onTargetDurationChange: (Int?) -> Unit,
    val onTargetStartDateChange: (Long?) -> Unit,
    val onSubmit: (() -> Unit) -> Unit,
    val onAccountTypeChange: (AccountType) -> Unit,
)