package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.TargetEntity

@Dao
interface TargetDao {
    @Upsert
    suspend fun saveTarget(target: TargetEntity)

    @Query("SELECT * FROM targets WHERE id = :id")
    suspend fun getTargetById(id: String): TargetEntity?

    @Update
    suspend fun updateTarget(target: TargetEntity)

    @Query("SELECT * FROM targets")
    suspend fun getAllTargets(): List<TargetEntity>

    @Delete
    suspend fun deleteTarget(entity: TargetEntity)
}