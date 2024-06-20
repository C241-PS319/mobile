package com.c241ps319.patera.data.model

import com.google.gson.annotations.SerializedName

data class LoginGoogleResponse(

    @field:SerializedName("data")
    val data: LoginGoogleData,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class LoginGoogleData(

    @field:SerializedName("token")
    val token: String
)
