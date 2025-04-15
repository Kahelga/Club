package com.example.club.feature.auth.presentation

import com.example.shared.user.auth.domain.entity.AuthResponse

sealed interface AuthState {
    data object Initial : AuthState
    data class Failure(val message: String?) : AuthState
    data class Success(val response: AuthResponse) :
        AuthState
    data object LoggedOut : AuthState
}