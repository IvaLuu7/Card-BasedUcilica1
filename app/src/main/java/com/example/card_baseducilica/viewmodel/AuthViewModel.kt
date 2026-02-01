package com.example.card_baseducilica.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.card_baseducilica.data.AppDatabase
import com.example.card_baseducilica.data.AuthRepository
import com.example.card_baseducilica.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val userDao = AppDatabase
        .getInstance(application)
        .userDao()

    private val authRepository = AuthRepository(userDao)

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                val userId: Int = authRepository.login(username, password)
                sessionManager.saveUserId(userId)
                _loginSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun register(username: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                _registerSuccess.value = false

                authRepository.register(username, password, confirmPassword)

                // ✅ ako je prošlo, javi UI-u
                _registerSuccess.value = true
            } catch (e: IllegalArgumentException) {
                _error.value = e.message
                _registerSuccess.value = false
            } catch (e: Exception) {
                _error.value = "Greška pri registraciji."
                _registerSuccess.value = false
            }
        }
    }

    fun checkSession() {
        val userId = sessionManager.getUserId()
        _loginSuccess.value = userId != null
    }

    fun clearError() {
        _error.value = null
    }
}