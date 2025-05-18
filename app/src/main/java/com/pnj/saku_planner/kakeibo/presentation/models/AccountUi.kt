package com.pnj.saku_planner.kakeibo.presentation.models

import com.pnj.saku_planner.core.database.entity.TargetUi

data class AccountUi(
    val id: Int,
    val name: String,
    val balance: Double,
    val target: TargetUi? = null,
    val description: String? = null,
)
