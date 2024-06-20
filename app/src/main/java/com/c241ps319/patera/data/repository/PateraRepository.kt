package com.c241ps319.patera.data.repository

import android.util.Log
import androidx.lifecycle.liveData
import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.data.local.DataStoreManager
import com.c241ps319.patera.data.model.LoginResponse
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.data.remote.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class PateraRepository private constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

//    private val imageClassifierHelper = ImageClassifierHelper
//
//    fun classifyImage(uri: Uri) = liveData {
//        emit(Result.Loading)
//        try {
//            val outputs = imageClassifierHelper.classifyStaticImage(uri)
//            emit(Result.Success(outputs))
//        } catch (e: Exception) {
//            emit(Result.Error(e))
//        }
//    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun getUser(token: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getUser("Bearer $token")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun updateProfile(token: String, name: String, email: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.updateProfile(token = "Bearer $token", name, email)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun saveSession(user: UserModel) {
        Log.d(TAG, "saveSession: $user")
        dataStoreManager.saveSession(user)
    }

    // Get Session
    val session: Flow<UserModel?> = dataStoreManager.session

    // Clear Session
    suspend fun logout() {
        dataStoreManager.clearData()
    }

    companion object {
        private val TAG = PateraRepository::class.java.simpleName

        @Volatile
        private var instance: PateraRepository? = null
        fun getInstance(
            apiService: ApiService,
            dataStoreManager: DataStoreManager
        ): PateraRepository = instance ?: synchronized(this) {
            instance ?: PateraRepository(apiService, dataStoreManager)
        }.also { instance = it }
    }


}