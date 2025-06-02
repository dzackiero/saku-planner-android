package com.pnj.saku_planner.kakeibo.presentation.models

import java.time.YearMonth

data class ReflectionUi(
    val id: String,
    val yearMonth: YearMonth = YearMonth.now(),

    val favoriteTransactionId: String? = null,
    val regretTransactionId: String? = null,

    val savingFeeling: String? = null,
    val savingNote: String? = null,

    val currentMonthNote: String? = null,
    val nextMonthNote: String? = null,
)