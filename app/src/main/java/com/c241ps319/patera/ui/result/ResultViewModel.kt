package com.c241ps319.patera.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.c241ps319.patera.BuildConfig
import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.data.model.UploadImageResponse
import com.c241ps319.patera.data.remote.ApiService
import com.c241ps319.patera.data.repository.PateraRepository
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UploadImageViewModel : ViewModel() {

    private val retrofit: Retrofit by lazy {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun uploadImage(image: MultipartBody.Part) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.uploadImage(image)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadImageResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown Error"))
        }
    }
}

class RecommendationViewModel(private val pateraRepository: PateraRepository) : ViewModel() {
    fun getRecommendation(token: String, pathLabel: Int, picture: String) =
        pateraRepository.getRecommendation(token, pathLabel, picture)
}
