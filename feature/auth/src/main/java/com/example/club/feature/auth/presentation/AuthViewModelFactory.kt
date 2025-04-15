package com.example.club.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AuthViewModelFactory (
    private val authUseCase: com.example.shared.user.auth.domain.usecase.AuthUseCase,
    private val tokenManager: com.example.club.util.manager.token.TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == AuthViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return AuthViewModel(authUseCase,tokenManager) as T
    }
}