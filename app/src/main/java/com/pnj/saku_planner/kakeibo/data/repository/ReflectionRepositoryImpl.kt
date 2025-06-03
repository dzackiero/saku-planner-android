package com.pnj.saku_planner.kakeibo.data.repository

import com.pnj.saku_planner.core.database.dao.ReflectionDao
import com.pnj.saku_planner.core.database.entity.ReflectionEntity
import com.pnj.saku_planner.kakeibo.domain.repository.ReflectionRepository
import javax.inject.Inject

class ReflectionRepositoryImpl @Inject constructor(
    private val reflectionDao: ReflectionDao
) : ReflectionRepository {
    override suspend fun saveReflection(reflection: ReflectionEntity) {
        reflectionDao.saveReflection(reflection)
    }

    override suspend fun getReflectionById(id: String): ReflectionEntity? {
        return reflectionDao.getReflectionById(id)
    }

    override suspend fun getAllReflections(): List<ReflectionEntity> {
        return reflectionDao.getAllReflections()
    }

    override suspend fun deleteReflection(id: String) {
        return reflectionDao.deleteReflection(id)
    }

}