package com.example.card_baseducilica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.card_baseducilica.data.AppDatabase
import com.example.card_baseducilica.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase
        .getInstance(application)
        .userDao()

    private val authRepository = AuthRepository(userDao)

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                authRepository.login(username, password)
                _error.value = null
                _loginSuccess.value = true
            } catch (e: IllegalArgumentException) {
                _error.value = e.message
                _loginSuccess.value = false
            }
        }
    }

    fun register(
        username: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            try {
                authRepository.register(username, password, confirmPassword)
                _error.value = null
            } catch (e: IllegalArgumentException) {
                _error.value = e.message
            }
        }
    }
}