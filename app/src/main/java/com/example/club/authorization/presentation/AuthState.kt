package com.example.club.authorization.presentation

import com.example.club.authorization.domain.AuthResponse

sealed interface AuthState {
    data object Initial : AuthState
    data class Failure(val message: String?) : AuthState
    data class Success(val response: AuthResponse) : AuthState
}