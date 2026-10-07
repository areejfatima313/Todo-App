  package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.AppDatabase
import com.example.todoapp.data.UserRepository
import com.example.todoapp.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = UserRepository(AppDatabase.getDatabase(app).userDao())
    private val session = SessionManager(app)

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            when (val r = repo.register(name, email, password)) {
                is UserRepository.AuthResult.Success -> {
                    session.saveLogin(r.user.id, r.user.name, r.user.email)
                    _uiState.value = AuthUiState.Success(r.user.name)
                }
                is UserRepository.AuthResult.Error ->
                    _uiState.value = AuthUiState.Error(r.message)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            when (val r = repo.login(email, password)) {
                is UserRepository.AuthResult.Success -> {
                    session.saveLogin(r.user.id, r.user.name, r.user.email)
                    _uiState.value = AuthUiState.Success(r.user.name)
                }
                is UserRepository.AuthResult.Error ->
                    _uiState.value = AuthUiState.Error(r.message)
            }
        }
    }

    fun logout() {
        session.logout()
        _uiState.value = AuthUiState.Idle
    }

    fun clearState() {
        _uiState.value = AuthUiState.Idle
    }

    fun isLoggedIn() = session.isLoggedIn()
    fun currentUserId() = session.getUserId()
    fun currentUserName() = session.getUserName()
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val name: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}