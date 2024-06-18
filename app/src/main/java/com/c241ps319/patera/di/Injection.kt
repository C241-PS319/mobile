package com.c241ps319.patera.di

import android.content.Context
import com.c241ps319.patera.data.repository.PateraRepository

object Injection {
    fun provideRepository(ctx: Context): PateraRepository {
//        val apiService = ApiService.getApu
        return PateraRepository.getInstance(ctx)
    }
}