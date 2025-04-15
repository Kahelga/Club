package com.example.club.feature.admin.events.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.shared.event.domain.usecase.GetEventsAdminUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EventsViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val getEventsAdminUseCase: GetEventsAdminUseCase,
    ) : ViewModel() {
    private val _state = MutableStateFlow<EventsState>(EventsState.Initial)
    val state: StateFlow<EventsState> = _state
    private var accessToken: String? = null
    fun loadEvents() {
        viewModelScope.launch {
            _state.value = EventsState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val events = getEventsAdminUseCase("Bearer $accessToken")
                _state.value = EventsState.Content(events)
            } catch (ex: Exception) {
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)
                            val events = getEventsAdminUseCase("Bearer $accessToken")
                            _state.value = EventsState.Content(events)
                        } else {
                            _state.value = EventsState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value = EventsState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = EventsState.Failure(ex.message)
                }
            }
        }
    }
}