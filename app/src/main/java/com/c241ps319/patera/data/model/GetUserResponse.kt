package com.c241ps319.patera.data.model

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

    @field:SerializedName("data")
    val userData: UserData,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String
)

data class UserData(

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("picture")
    val picture: String? = null
)
