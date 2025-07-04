package com.pnj.saku_planner.kakeibo.presentation.models

import java.time.YearMonth

data class MonthlySummary(
    val yearMonth: YearMonth,
    val income: Double,
    val expense: Double,
    val savingsRatio: Float
)
