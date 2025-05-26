package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.TargetEntity

@Dao
interface TargetDao {
    @Upsert
    suspend fun upsertTarget(target: TargetEntity)

    suspend fun saveTarget(target: TargetEntity) {
        val targetToSave = target.copy(
            updatedAt = System.currentTimeMillis()
        )
        upsertTarget(targetToSave)
    }

    @Query("SELECT * FROM targets WHERE id = :id")
    suspend fun getTargetById(id: String): TargetEntity?

    @Update
    suspend fun updateTarget(target: TargetEntity)

    @Query("SELECT * FROM targets WHERE isDeleted = 0")
    suspend fun getAllTargets(): List<TargetEntity>

    @Query("UPDATE targets SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun deleteTarget(id: String, timestamp: Long = System.currentTimeMillis())

    // --- Sync Methods ---
    @Query("SELECT * FROM targets WHERE (syncedAt IS NULL OR updatedAt > syncedAt) AND isDeleted = 0")
    suspend fun getTargetsToUpsert(): List<TargetEntity>

    @Query("SELECT id FROM targets WHERE isDeleted = 1")
    suspend fun getDeletedTargetIds(): List<String>

    @Query("UPDATE targets SET syncedAt = :timestamp WHERE id IN (:ids)")
    suspend fun markTargetsAsSynced(ids: List<String>, timestamp: Long)

    @Query("DELETE FROM targets WHERE id IN (:ids)")
    suspend fun hardDeleteTargets(ids: List<String>)

}