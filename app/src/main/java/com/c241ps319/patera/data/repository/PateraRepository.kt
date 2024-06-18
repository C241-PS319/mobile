package com.c241ps319.patera.data.repository

import android.content.Context

class PateraRepository private constructor(ctx: Context) {
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



    companion object {
        @Volatile
        private var instance: PateraRepository? = null
        fun getInstance(ctx: Context): PateraRepository = instance ?: synchronized(this) {
            instance ?: PateraRepository(ctx)
        }.also { instance = it }
    }

}