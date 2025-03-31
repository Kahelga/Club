package com.example.club.profileUpdate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.profile.presentation.ProfileViewModel
import com.example.club.profileUpdate.domain.usecase.UpdateProfileUseCase

class ProfileUpdateViewModelFactory(
    private val login:String,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ProfileUpdateViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return ProfileUpdateViewModel(login,updateProfileUseCase,refreshTokenUseCase,tokenManager) as T
    }
}