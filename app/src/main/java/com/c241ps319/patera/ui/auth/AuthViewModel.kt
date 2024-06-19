package com.c241ps319.patera.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.data.repository.PateraRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val pateraRepository: PateraRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        pateraRepository.register(name, email, password)

    fun login(email: String, password: String) =
        pateraRepository.login(email, password)

    fun getUser(token: String) = pateraRepository.getUser(token)

    fun saveSession(userModel: UserModel) = viewModelScope.launch {
        pateraRepository.saveSession(userModel)
    }

}