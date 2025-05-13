package com.pnj.saku_planner.kakeibo.presentation.models

import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.domain.model.Transaction
import java.time.LocalDate

data class TransactionUi(
    val id: Int,
    val date: LocalDate,
    val icon: String,
    val description: String,
    val wallet: String,
    val type: TransactionType,
    val amount: Long
)
