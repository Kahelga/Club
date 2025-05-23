package com.example.club.feature.profileupdate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.shared.user.profile.domain.usecase.UpdateProfileUseCase

class ProfileUpdateViewModelFactory(
    private val login:String,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: com.example.club.util.manager.token.TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ProfileUpdateViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return ProfileUpdateViewModel(login,updateProfileUseCase,refreshTokenUseCase,tokenManager) as T
    }
}