package com.pnj.saku_planner.kakeibo.presentation.models

import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import java.time.LocalDate

data class TransactionUi(
    val id: Int,
    val date: LocalDate,
    val icon: String?,
    val description: String,
    val account: String,
    val toAccount: String?,
    val category: String?,
    val type: TransactionType,
    val kakeibo: KakeiboCategoryType,
    val amount: Double
)
