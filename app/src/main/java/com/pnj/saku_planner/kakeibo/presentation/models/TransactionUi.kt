package com.pnj.saku_planner.kakeibo.presentation.models

import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import java.time.LocalDateTime

data class TransactionUi(
    val id: String,
    val date: LocalDateTime,
    val icon: String? = null,
    val description: String = "",
    val account: String,
    val toAccount: String? = null,
    val category: String? = null,
    val type: TransactionType,
    val kakeibo: KakeiboCategoryType? = null,
    val amount: Long
)
