package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pnj.saku_planner.core.database.entity.TargetEntity

@Dao
interface TargetDao {
    @Insert
    suspend fun insertTarget(target: TargetEntity): Long

    @Query("SELECT * FROM targets WHERE id = :id")
    suspend fun getTargetById(id: Int): TargetEntity?

    @Update
    suspend fun updateTarget(target: TargetEntity)

    @Query("SELECT * FROM targets")
    suspend fun getAllTargets(): List<TargetEntity>

    @Delete
    suspend fun deleteTarget(entity: TargetEntity)
}