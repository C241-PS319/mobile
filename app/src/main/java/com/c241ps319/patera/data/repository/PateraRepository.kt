package com.c241ps319.patera.data.repository

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
            try {
                // Save Token
                saveToken(successResponse.data.token)
            } catch (e: Exception) {
                emit(ResultState.Error(e.message))
            }
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
            val successResponse = apiService.getUser(token)
            try {
                // Save Session
                saveSession(
                    UserModel(
                        name = successResponse.userData.name,
                        email = successResponse.userData.email,
                        phone = successResponse.userData.phone,
                        picture = successResponse.userData.picture,
                        token = token,
                        isLogin = true
                    )
                )
            } catch (e: Exception) {
                emit(ResultState.Error(e.message))
            }
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    suspend fun saveToken(token: String) {
        dataStoreManager.saveToken(token)
    }

    suspend fun saveSession(user: UserModel) {
        dataStoreManager.saveSession(user)
    }

    // Get Session
    val session: Flow<UserModel?> = dataStoreManager.session

    // Clear Session
    suspend fun logout() {
        dataStoreManager.clearData()
    }

    companion object {
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