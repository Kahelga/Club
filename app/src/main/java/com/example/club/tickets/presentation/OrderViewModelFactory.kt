package com.example.club.tickets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.profile.presentation.ProfileViewModel
import com.example.club.tickets.domain.usecase.GetOrderUseCase

class OrderViewModelFactory(
    private val getOrderUseCase: GetOrderUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == OrderViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return OrderViewModel(getOrderUseCase,refreshTokenUseCase,tokenManager) as T
    }
}