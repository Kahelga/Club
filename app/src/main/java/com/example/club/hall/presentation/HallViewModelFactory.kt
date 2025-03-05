package com.example.club.hall.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.hall.domain.usecase.GetHallUseCase
import com.example.club.profile.presentation.ProfileViewModel

class HallViewModelFactory(
    private val eventId: String,
    private val getHallUseCase: GetHallUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == HallViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return HallViewModel(eventId,getHallUseCase,refreshTokenUseCase,tokenManager) as T
    }
}