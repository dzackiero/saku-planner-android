package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.database.entity.ReflectionEntity

interface ReflectionRepository {
    suspend fun saveReflection(reflection: ReflectionEntity)

    suspend fun getReflectionById(id: String): ReflectionEntity?

    suspend fun getAllReflections(): List<ReflectionEntity>

    suspend fun deleteReflection(id: String)
}