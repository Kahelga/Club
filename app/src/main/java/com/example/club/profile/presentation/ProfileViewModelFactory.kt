package com.example.club.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.eventDetails.presentation.EventDetailsViewModel
import com.example.club.profile.domain.usecase.GetProfileUseCase

class ProfileViewModelFactory(
    private val login:String,
    private val getProfileUseCase: GetProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ProfileViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return ProfileViewModel(login,getProfileUseCase,refreshTokenUseCase,tokenManager) as T
    }
}