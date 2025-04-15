package com.example.club.feature.profile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.shared.user.profile.domain.entity.User
import com.example.club.shared.user.profile.domain.usecase.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(
    private var login: String,
    private val getProfileUseCase: GetProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Initial)
    val state: StateFlow<ProfileState> = _state
    private var accessToken: String? = null
    private val _user = MutableStateFlow<User>(
        User("", "", "", "", "", "", "", ""),
    )
    val user: StateFlow<User> = _user

    fun loadUser() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val response = getProfileUseCase(login, "Bearer $accessToken")
                _user.value = response
                _state.value = ProfileState.Content(response)

            } catch (ex: Exception) {
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)

                            val response = getProfileUseCase(login, "Bearer $accessToken")
                            _user.value = response
                            _state.value = ProfileState.Content(response)
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
 fun setLogin(newLogin: String){
        login=newLogin
    }
}