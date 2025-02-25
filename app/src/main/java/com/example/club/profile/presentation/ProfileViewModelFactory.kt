package com.example.club.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.eventDetails.presentation.EventDetailsViewModel
import com.example.club.profile.domain.usecase.GetProfileUseCase

class ProfileViewModelFactory(
    private val userId:String,
    private val getProfileUseCase: GetProfileUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ProfileViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return ProfileViewModel(userId,getProfileUseCase) as T
    }
}