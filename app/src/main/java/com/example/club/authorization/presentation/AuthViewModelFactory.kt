package com.example.club.authorization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.AuthUseCase
import com.example.club.poster.presentation.PosterViewModel

class AuthViewModelFactory (
    private val authUseCase: AuthUseCase,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == AuthViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return AuthViewModel(authUseCase,tokenManager) as T
    }
}