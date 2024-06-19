package com.c241ps319.patera.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c241ps319.patera.data.repository.PateraRepository
import com.c241ps319.patera.di.Injection
import com.c241ps319.patera.ui.auth.AuthViewModel
import com.c241ps319.patera.ui.main.MainViewModel
import com.c241ps319.patera.ui.scan.ScanViewModel

class ViewModelFactory private constructor(private val pateraRepository: PateraRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                ScanViewModel(pateraRepository) as T
            }

            modelClass.isAssignableFrom(ScanViewModel::class.java) -> {
                ScanViewModel(pateraRepository) as T
            }

            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                ScanViewModel(pateraRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class : " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also {
            instance = it
        }

    }
}