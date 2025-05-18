package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("targets")
class TargetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val duration: Int = 1,
    val startDate: Long? = null,
    val targetAmount: Double = 0.0,

    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
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
    val id: Int = 0,
    val duration: Int = 1,
    val startDate: Long? = null,
    val targetAmount: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis(),
)