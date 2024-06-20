package com.c241ps319.patera.data.remote

import com.c241ps319.patera.data.model.GetUserResponse
import com.c241ps319.patera.data.model.LoginGoogleResponse
import com.c241ps319.patera.data.model.LoginResponse
import com.c241ps319.patera.data.model.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register/")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login/")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @FormUrlEncoded
    @POST("auth/google-auth/")
    suspend fun loginGoogle(
        @Field("firebase_id_token") firebaseToken: String,
    ): LoginGoogleResponse

    @GET("auth/user/")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): GetUserResponse

    @FormUrlEncoded
    @PUT("auth/user/edit/")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("email") email: String,
    ): GetUserResponse
}