package com.c241ps319.patera.data.remote

import com.c241ps319.patera.data.model.ErrorResponse
import com.c241ps319.patera.data.model.GetUserResponse
import com.c241ps319.patera.data.model.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register/")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): ErrorResponse

    @FormUrlEncoded
    @POST("auth/login/")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("auth/user/")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): GetUserResponse
}