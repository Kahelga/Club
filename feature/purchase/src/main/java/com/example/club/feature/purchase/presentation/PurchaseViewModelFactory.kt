package com.example.club.feature.purchase.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.shared.tickets.domain.usecase.PurchaseUseCase

class PurchaseViewModelFactory(
    private val purchaseUseCase: PurchaseUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: com.example.club.util.manager.token.TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == PurchaseViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return PurchaseViewModel(purchaseUseCase,refreshTokenUseCase,tokenManager) as T
    }
}