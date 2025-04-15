package com.example.club.feature.hall.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.shared.event.domain.usecase.GetHallUseCase

class HallViewModelFactory(
    private val eventId: String,
    private val getHallUseCase: GetHallUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: com.example.club.util.manager.token.TokenManager
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == HallViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return HallViewModel(eventId,getHallUseCase,refreshTokenUseCase,tokenManager) as T
    }
}