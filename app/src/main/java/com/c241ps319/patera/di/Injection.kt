package com.c241ps319.patera.di

import android.content.Context
import com.c241ps319.patera.data.local.DataStoreManager
import com.c241ps319.patera.data.local.dataStore
import com.c241ps319.patera.data.remote.ApiConfig
import com.c241ps319.patera.data.repository.PateraRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(ctx: Context): PateraRepository {
        val apiService = ApiConfig.getApiService()
        val dataStoreManager = DataStoreManager.getInstance(ctx.dataStore)
        val user = runBlocking { dataStoreManager.loginResultFlow.first() }
        return PateraRepository.getInstance(apiService, dataStoreManager)
    }
}