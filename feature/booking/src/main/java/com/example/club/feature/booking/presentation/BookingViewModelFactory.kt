package com.example.club.feature.booking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.shared.tickets.domain.usecase.BookingUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase

class BookingViewModelFactory(
    private val bookingUseCase: BookingUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == BookingViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return BookingViewModel(bookingUseCase,refreshTokenUseCase,tokenManager) as T
    }
}