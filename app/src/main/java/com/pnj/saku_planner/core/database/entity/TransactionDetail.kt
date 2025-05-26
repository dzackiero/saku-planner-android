package com.pnj.saku_planner.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.pnj.saku_planner.kakeibo.domain.enum.KakeiboCategoryType
import com.pnj.saku_planner.kakeibo.domain.enum.TransactionType
import com.pnj.saku_planner.kakeibo.presentation.models.TransactionUi
import java.time.Instant
import java.time.ZoneId

data class TransactionDetail(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id",
    )
    val account: AccountEntity,

    @Relation(
        parentColumn = "toAccountId",
        entityColumn = "id",
    )
    val toAccount: AccountEntity?,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id",
    )
    val category: CategoryEntity?,
)

fun TransactionDetail.toUi() = TransactionUi(
    id = transaction.id,
    icon = category?.icon,
    account = account.name,
    toAccount = toAccount?.name,
    category = category?.name,
    amount = transaction.amount,
    description = transaction.description,
    type = TransactionType.valueOf(transaction.type.uppercase()),
    kakeibo = if (transaction.kakeiboCategory != null) KakeiboCategoryType.valueOf(transaction.kakeiboCategory.uppercase()) else null,
    date = Instant.ofEpochMilli(transaction.transactionAt).atZone(ZoneId.systemDefault())
        .toLocalDateTime(),
)

data class TransactionCategorySummary(
    val name: String,
    val amount: Double,
    val icon: String? = null
)