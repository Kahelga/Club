package com.example.club.profile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val login:String,
    private val getProfileUseCase: GetProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
):ViewModel() {
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state: StateFlow<ProfileState> = _state
    private var accessToken: String? = null

    fun loadUser () {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val user = getProfileUseCase(login, "Bearer $accessToken")
                _state.value = ProfileState.Content(user)

            } catch (ex: Exception) {
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)

                            val user = getProfileUseCase(login, "Bearer $accessToken")
                            _state.value = ProfileState.Content(user)
                        } else {
                            _state.value = ProfileState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value = ProfileState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = ProfileState.Failure(ex.message)
                }
            }
        }
    }
}