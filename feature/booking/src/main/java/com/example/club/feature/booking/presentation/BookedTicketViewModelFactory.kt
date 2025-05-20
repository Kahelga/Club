package com.example.club.feature.booking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.shared.tickets.domain.usecase.GetBookedTicketUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase

class BookedTicketViewModelFactory(
    private val bookedId:String,
    private val getBookedTicketUseCase: GetBookedTicketUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == BookedTicketViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return BookedTicketViewModel(bookedId,getBookedTicketUseCase,refreshTokenUseCase,tokenManager) as T
    }
}