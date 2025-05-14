package com.pnj.saku_planner.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi

@Entity(tableName = "accounts")
class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val balance: Double,
    val description: String? = null,
    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

fun AccountEntity.toUi(): AccountUi {
    return AccountUi(
        id = id,
        name = name,
        balance = balance,
    )
}

