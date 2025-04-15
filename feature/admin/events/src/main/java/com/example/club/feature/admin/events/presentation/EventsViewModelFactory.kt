package com.example.club.feature.admin.events.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.shared.event.domain.usecase.GetEventsAdminUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase

class EventsViewModelFactory(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val getEventsAdminUseCase: GetEventsAdminUseCase,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == EventsViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return EventsViewModel(refreshTokenUseCase,tokenManager,getEventsAdminUseCase) as T
    }
}