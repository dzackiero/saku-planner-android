package com.pnj.saku_planner.kakeibo.presentation.screens.accounts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pnj.saku_planner.core.database.entity.AccountEntity
import com.pnj.saku_planner.core.database.entity.TargetEntity
import com.pnj.saku_planner.core.database.entity.toUi
import com.pnj.saku_planner.core.util.validateRequired
import com.pnj.saku_planner.kakeibo.domain.enum.AccountType
import com.pnj.saku_planner.kakeibo.domain.repository.AccountRepository
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
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

    fun loadAccount(accountId: String) {
        viewModelScope.launch {
            val account = accountRepository.getAccountById(accountId)?.toUi() ?: return@launch

            _formState.value = _formState.value.copy(
                accountId = account.id,
                accountName = account.name,
                currentBalance = account.balance,
                accountType = if (account.target == null) AccountType.Checking else AccountType.Savings,
                targetAmount = account.target?.targetAmount,
                targetDuration = account.target?.duration,
                targetStartDate = account.target?.startDate,
                description = account.description ?: "",
            )
        }
    }

    fun deleteAccount() {
        val accountId = _formState.value.accountId ?: return
        viewModelScope.launch {
            accountRepository.deleteAccount(accountId)
        }
    }

    private fun submit(): Boolean {
        if (validateForm()) return false

        viewModelScope.launch {
            val values = _formState.value
            val accountEntity = AccountEntity(
                id = values.accountId ?: UUID.randomUUID().toString(),
                name = values.accountName,
                balance = values.currentBalance ?: 0,
                description = values.description,
            )

            var targetEntity: TargetEntity? = null
            if (values.accountType == AccountType.Savings) {
                targetEntity = TargetEntity(
                    id = randomUuid(),
                    duration = values.targetDuration ?: 0,
                    startDate = values.targetStartDate ?: 0,
                    targetAmount = values.targetAmount ?: 0,
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
    val accountId: String? = null,

    val accountName: String = "",
    val accountNameError: String? = null,

    val currentBalance: Long? = null,
    val currentBalanceError: String? = null,

    val accountType: AccountType = AccountType.Checking,
    val accountTypeError: String? = null,

    val targetAmount: Long? = null,
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
    val onCurrentBalanceChange: (Long?) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onTargetAmountChange: (Long?) -> Unit,
    val onTargetDurationChange: (Int?) -> Unit,
    val onTargetStartDateChange: (Long?) -> Unit,
    val onSubmit: (() -> Unit) -> Unit,
    val onAccountTypeChange: (AccountType) -> Unit,
)