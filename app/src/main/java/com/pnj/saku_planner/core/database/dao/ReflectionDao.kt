package com.pnj.saku_planner.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.pnj.saku_planner.core.database.entity.ReflectionEntity

@Dao
interface ReflectionDao {
    @Upsert
    suspend fun upsertReflection(reflection: ReflectionEntity)

    suspend fun saveReflection(reflection: ReflectionEntity) {
        val reflectionToSave = reflection.copy(
            updatedAt = System.currentTimeMillis()
        )
        upsertReflection(reflectionToSave)
    }

    @Query("SELECT * FROM reflections WHERE isDeleted = 0")
    suspend fun getAllReflections(): List<ReflectionEntity>

    @Query("SELECT * FROM reflections WHERE id = :id")
    suspend fun getReflectionById(id: String): ReflectionEntity?

    @Query("UPDATE reflections SET isDeleted = 1, updatedAt = :timestamp WHERE id = :id")
    suspend fun deleteReflection(id: String, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT * FROM reflections WHERE (syncedAt IS NULL OR updatedAt > syncedAt) AND isDeleted = 0")
    suspend fun getReflectionsToUpsert(): List<ReflectionEntity>

    @Query("SELECT id FROM reflections WHERE isDeleted = 1")
    suspend fun getDeletedReflectionIds(): List<String>

    @Query("UPDATE reflections SET syncedAt = :timestamp WHERE id IN (:ids)")
    suspend fun markReflectionsAsSynced(ids: List<String>, timestamp: Long)

    @Query("DELETE FROM reflections WHERE id IN (:ids)")
    suspend fun hardDeleteReflections(ids: List<String>)
}