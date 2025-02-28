package com.example.club.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.eventDetails.presentation.EventDetailsState
import com.example.club.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ProfileViewModel(
    private val login:String,
    private val getProfileUseCase: GetProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
):ViewModel() {
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state: StateFlow<ProfileState> = _state
    private var accessToken: String? = null

    fun loadUser(){
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                if (accessToken == null || tokenManager.isAccessTokenExpired()) {
                    val refreshToken = tokenManager.getRefreshToken()
                    if (refreshToken != null) {
                        val newToken=refreshTokenUseCase(refreshToken)
                        accessToken =newToken.accessToken
                        tokenManager.updateAccessToken(accessToken)
                    }
                }
                val user = getProfileUseCase(login,"Bearer $accessToken")
                _state.value = ProfileState.Content(user)
            } catch (ce: CancellationException) {
                throw ce
            } catch (ex: Exception) {
                _state.value = ProfileState.Failure(ex.message)
            }
        }
    }
}