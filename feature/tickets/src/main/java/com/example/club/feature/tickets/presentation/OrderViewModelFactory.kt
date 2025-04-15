package com.example.club.feature.tickets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.shared.tickets.domain.usecase.GetOrderUseCase

class OrderViewModelFactory(
    private val getOrderUseCase: GetOrderUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: com.example.club.util.manager.token.TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == OrderViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return OrderViewModel(getOrderUseCase,refreshTokenUseCase,tokenManager) as T
    }
}