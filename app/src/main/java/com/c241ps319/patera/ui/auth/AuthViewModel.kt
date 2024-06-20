package com.c241ps319.patera.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.data.repository.PateraRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel(private val pateraRepository: PateraRepository) : ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(name: String, email: String, password: String) =
        pateraRepository.register(name, email, password)

    fun login(email: String, password: String) =
        pateraRepository.login(email, password)

    fun loginGoogle(firebaseToken: String) = pateraRepository.loginGoogle(firebaseToken)

    fun getUser(token: String) = pateraRepository.getUser(token)

    fun saveSession(userModel: UserModel) = viewModelScope.launch {
        pateraRepository.saveSession(userModel)
    }

    fun getSession(): LiveData<UserModel?> {
        return pateraRepository.session.asLiveData()
    }

}