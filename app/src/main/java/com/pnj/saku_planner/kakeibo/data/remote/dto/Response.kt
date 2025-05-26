package com.pnj.saku_planner.kakeibo.data.remote.dto

data class Response<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null
)
