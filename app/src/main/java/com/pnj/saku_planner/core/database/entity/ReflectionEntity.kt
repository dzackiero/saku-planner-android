package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pnj.saku_planner.kakeibo.presentation.components.ui.randomUuid
import com.pnj.saku_planner.kakeibo.presentation.models.ReflectionUi
import java.time.YearMonth

@Entity(tableName = "reflections")
data class ReflectionEntity(
    @PrimaryKey
    val id: String = randomUuid(),
    val year: Int = 0,
    val month: Int = 0,
    val favoriteTransactionId: String? = null,
    val regretTransactionId: String? = null,
    val savingFeeling: String? = null,
    val savingNote: String? = null,
    val currentMonthNote: String? = null,
    val nextMonthNote: String? = null,

    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
)

fun ReflectionEntity.toUi() = ReflectionUi(
    id = id,
    yearMonth = YearMonth.of(year, month),
    savingFeeling = savingFeeling,
    savingNote = savingNote,
    favoriteTransactionId = favoriteTransactionId,
    regretTransactionId = regretTransactionId,
    currentMonthNote = currentMonthNote,
    nextMonthNote = nextMonthNote,
)