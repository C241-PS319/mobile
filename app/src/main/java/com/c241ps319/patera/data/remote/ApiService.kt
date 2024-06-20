package com.c241ps319.patera.data.remote

import com.c241ps319.patera.data.model.GetHistoriesResponse
import com.c241ps319.patera.data.model.GetUserResponse
import com.c241ps319.patera.data.model.LoginResponse
import com.c241ps319.patera.data.model.RecommendationResponse
import com.c241ps319.patera.data.model.RegisterResponse
import com.c241ps319.patera.data.model.UploadImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

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

    @GET("user-history/")
    suspend fun getHistories(
        @Header("Authorization") token: String
    ): GetHistoriesResponse

    @Multipart
    @POST("index.php")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): UploadImageResponse

    @FormUrlEncoded
    @POST("recommendation/{label}/")
    suspend fun postRecommendation(
        @Header("Authorization") token: String,
        @Path("label") label: Int,
        @Field("picture") picture: String,
    ): RecommendationResponse
}