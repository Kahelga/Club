package com.example.club.feature.tickets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.shared.tickets.domain.usecase.CancelOrderUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase

class CancelOrderViewModelFactory(
   /* private val bookingId: String,*/
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == CancelOrderViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return CancelOrderViewModel(/*bookingId,*/cancelOrderUseCase,refreshTokenUseCase,tokenManager) as T
    }
}