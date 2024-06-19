package com.c241ps319.patera.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.data.repository.PateraRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val pateraRepository: PateraRepository) : ViewModel() {
    fun updateProfile(token: String, name: String, email: String) =
        pateraRepository.updateProfile(token, name, email)

    fun updateSession(userModel: UserModel) = viewModelScope.launch {
        pateraRepository.saveSession(userModel)
    }
}