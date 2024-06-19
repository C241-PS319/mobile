package com.c241ps319.patera.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
    val data: LoginData,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class LoginData(

    @field:SerializedName("token")
    val token: String
)
