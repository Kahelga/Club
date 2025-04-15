package com.example.club.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.shared.user.profile.domain.usecase.GetProfileUseCase

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