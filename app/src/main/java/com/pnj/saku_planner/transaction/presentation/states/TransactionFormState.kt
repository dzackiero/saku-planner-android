package com.pnj.saku_planner.transaction.presentation.states

import com.pnj.saku_planner.transaction.domain.enum.KakeiboCategory
import com.pnj.saku_planner.transaction.domain.enum.TransactionType
import com.pnj.saku_planner.transaction.presentation.models.CategoryUi

data class TransactionFormState(
    val transactionId: String? = null,
    val transactionType: TransactionType = TransactionType.INCOME,
    val selectedCategory: CategoryUi? = null,
    val selectedKakeibo: KakeiboCategory? = null,
    val selectedAccount: String = "",
    val amount: Double = 0.0,
    val description: String = ""
)

data class TransactionFormCallbacks(
    val onTransactionTypeChange: (TransactionType) -> Unit,
    val onCategoryChange: (CategoryUi) -> Unit,
    val onAccountChange: (String) -> Unit,
    val onKakeiboChange: (KakeiboCategory) -> Unit,
    val onAmountChange: (Double) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onSubmit: () -> Unit
)

