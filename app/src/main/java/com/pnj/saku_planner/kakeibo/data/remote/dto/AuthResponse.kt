package com.pnj.saku_planner.kakeibo.data.remote.dto

data class AuthResponse(
    val user: UserDto,
    val token: String,
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
)
