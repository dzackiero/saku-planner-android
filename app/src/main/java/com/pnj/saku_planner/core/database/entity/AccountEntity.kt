package com.pnj.saku_planner.core.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.pnj.saku_planner.core.util.randomUuid
import com.pnj.saku_planner.kakeibo.presentation.models.AccountUi

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey
    val id: String = randomUuid(),
    val name: String,
    val balance: Long = 0,
    val description: String? = null,

    val targetId: String? = null,

    val syncedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
)

data class AccountWithTarget(
    @Embedded
    val account: AccountEntity,
    @Relation(
        parentColumn = "targetId",
        entityColumn = "id",
    )
    val target: TargetEntity?,
)

fun AccountEntity.toUi(): AccountUi {
    return AccountUi(
        id = id,
        name = name,
        balance = balance,
        description = description,
    )
}

fun AccountWithTarget.toUi(): AccountUi {
    return AccountUi(
        id = account.id,
        name = account.name,
        balance = account.balance,
        description = account.description,
        target = target?.toUi(),
    )
}

