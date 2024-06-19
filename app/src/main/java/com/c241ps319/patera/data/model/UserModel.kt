package com.c241ps319.patera.data.model

data class UserModel(
    val name: String,
    val email: String,
    val picture: String? = null,
    val phone: String? = null,
    val token: String,
    val isLogin: Boolean = false
)
