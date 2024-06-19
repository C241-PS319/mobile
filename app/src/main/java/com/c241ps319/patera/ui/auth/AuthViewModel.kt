package com.c241ps319.patera.ui.auth

import androidx.lifecycle.ViewModel
import com.c241ps319.patera.data.repository.PateraRepository

class AuthViewModel(private val pateraRepository: PateraRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        pateraRepository.register(name, email, password)

    fun login(email: String, password: String) =
        pateraRepository.login(email, password)

}