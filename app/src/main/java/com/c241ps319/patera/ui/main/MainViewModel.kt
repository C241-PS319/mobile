package com.c241ps319.patera.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.c241ps319.patera.data.model.LoginResult
import com.c241ps319.patera.data.repository.PateraRepository
import kotlinx.coroutines.launch

class MainViewModel(private val pateraRepository: PateraRepository) : ViewModel() {
    fun getLoginResult(): LiveData<LoginResult?> {
        return pateraRepository.loginResultFlow.asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pateraRepository.logout()
        }
    }
}