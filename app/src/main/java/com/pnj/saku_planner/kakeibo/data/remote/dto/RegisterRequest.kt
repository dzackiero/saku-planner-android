package com.pnj.saku_planner.kakeibo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirmation")
    val confirmPassword: String
)

