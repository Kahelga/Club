package com.example.club.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.authorization.presentation.AuthViewModel
import com.example.club.registration.domain.usecase.RegUseCase

class RegViewModelFactory(
    private val regUseCase: RegUseCase
): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == RegViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return RegViewModel(regUseCase) as T
    }
}