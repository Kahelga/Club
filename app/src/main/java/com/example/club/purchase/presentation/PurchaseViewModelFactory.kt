package com.example.club.purchase.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.purchase.domain.usecase.PurchaseUseCase

class PurchaseViewModelFactory(
    private val purchaseUseCase: PurchaseUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == PurchaseViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return PurchaseViewModel(purchaseUseCase,refreshTokenUseCase,tokenManager) as T
    }
}