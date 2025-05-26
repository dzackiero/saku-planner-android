package com.pnj.saku_planner.kakeibo.domain.repository

import com.pnj.saku_planner.core.util.Resource
import com.pnj.saku_planner.kakeibo.data.remote.dto.ScanResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ScanRepository {
    suspend fun predict(image: File): Flow<Resource<ScanResponse>>
}