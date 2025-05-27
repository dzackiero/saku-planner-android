package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.TransactionEntity
import java.time.Instant

data class TransactionDto(
    val id: String,

    @SerializedName("account_id")
    val accountId: String,

    @SerializedName("to_account_id")
    val toAccountId: String?,

    @SerializedName("category_id")
    val categoryId: String?,

    val type: String,

    @SerializedName("kakeibo_category")
    val kakeiboCategory: String?,

    val amount: Long,
    val description: String,

    @SerializedName("transaction_at")
    val transactionAt: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String?
)


fun TransactionEntity.toDto(): TransactionDto {
    val isoCreatedAt = Instant.ofEpochMilli(this.createdAt).toString()
    val isoUpdatedAt = Instant.ofEpochMilli(this.updatedAt).toString()
    val isoTransactionAt = Instant.ofEpochMilli(this.transactionAt).toString()
    val isoDeletedAt: String? = if (this.isDeleted) {
        isoUpdatedAt
    } else {
        null
    }

    return TransactionDto(
        id = this.id,
        accountId = this.accountId,
        toAccountId = this.toAccountId,
        categoryId = this.categoryId,
        type = this.type,
        kakeiboCategory = this.kakeiboCategory,
        amount = this.amount,
        description = this.description,
        transactionAt = isoTransactionAt,
        createdAt = isoCreatedAt,
        updatedAt = isoUpdatedAt,
        deletedAt = isoDeletedAt
    )
}