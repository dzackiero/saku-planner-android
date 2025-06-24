package com.pnj.saku_planner.kakeibo.data.remote.dto.entity

import com.google.gson.annotations.SerializedName
import com.pnj.saku_planner.core.database.entity.TargetEntity // Assuming this is the correct path
import java.time.Instant

data class TargetDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("start_date")
    val startDate: String?,

    @SerializedName("target_amount")
    val targetAmount: Long,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String?
)

fun TargetEntity.toDto(): TargetDto {
    val isoCreatedAt = Instant.ofEpochMilli(this.createdAt).toString()
    val isoUpdatedAt = Instant.ofEpochMilli(this.updatedAt).toString()
    val isoStartDate = this.startDate?.let { Instant.ofEpochMilli(it).toString() }

    val isoDeletedAt: String? = if (this.isDeleted) {
        isoUpdatedAt
    } else {
        null
    }

    return TargetDto(
        id = this.id,
        duration = this.duration,
        startDate = isoStartDate,
        targetAmount = this.targetAmount,
        createdAt = isoCreatedAt,
        updatedAt = isoUpdatedAt,
        deletedAt = isoDeletedAt
    )
}

fun TargetDto.toEntity(): TargetEntity {
    val epochStartDate = this.startDate?.let { Instant.parse(it).toEpochMilli() } ?: 0L
    val epochCreatedAt = Instant.parse(this.createdAt).toEpochMilli()
    val epochUpdatedAt = Instant.parse(this.updatedAt).toEpochMilli()

    return TargetEntity(
        id = this.id,
        duration = this.duration,
        startDate = epochStartDate,
        targetAmount = this.targetAmount,
        createdAt = epochCreatedAt,
        updatedAt = epochUpdatedAt,
        isDeleted = this.deletedAt != null
    )
}
