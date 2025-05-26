package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid

@Entity("targets")
data class TargetEntity(
    @PrimaryKey
    val id: String = randomUuid(),

    val duration: Int = 1,
    val startDate: Long? = null,
    val targetAmount: Long = 0,

    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
)

fun TargetEntity.toUi(): TargetUi {
    return TargetUi(
        id = id,
        duration = duration,
        startDate = startDate,
        targetAmount = targetAmount,
        createdAt = createdAt,
    )
}

data class TargetUi(
    val id: String,
    val duration: Int = 1,
    val startDate: Long? = null,
    val targetAmount: Long = 0,
    val createdAt: Long = System.currentTimeMillis(),
)