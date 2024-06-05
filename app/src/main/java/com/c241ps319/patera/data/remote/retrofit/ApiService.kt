package com.c241ps319.patera.data.remote.retrofit

import com.c241ps319.patera.BuildConfig
import com.c241ps319.patera.data.remote.response.News
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String = "plant",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): News
}