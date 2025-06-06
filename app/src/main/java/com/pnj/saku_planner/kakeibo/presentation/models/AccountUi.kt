package com.pnj.saku_planner.kakeibo.presentation.models

import com.pnj.saku_planner.core.database.entity.TargetUi

data class AccountUi(
    val id: String,
    val name: String,
    val balance: Long,
    val target: TargetUi? = null,
    val description: String? = null,
)
