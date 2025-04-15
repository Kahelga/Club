package com.example.club.feature.profileupdate.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import com.example.club.shared.user.profile.domain.entity.User
import com.example.club.shared.user.profile.domain.usecase.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileUpdateViewModel(
    private val login:String,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow<ProfileUpdateState>(ProfileUpdateState.Initial)
    val state: StateFlow<ProfileUpdateState> = _state
    private var accessToken: String? = null
    fun updateUser(newUser: User){
        viewModelScope.launch {
            _state.value = ProfileUpdateState.Loading
            try{
                accessToken = tokenManager.getAccessToken()
                val user=updateProfileUseCase(login,"Bearer $accessToken",newUser)
                _state.value = ProfileUpdateState.Success(user)
            }catch (ex: Exception){
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {
                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)
                            val user=updateProfileUseCase(login,"Bearer $accessToken",newUser)
                            _state.value = ProfileUpdateState.Success(user)
                        } else {
                            _state.value =
                                ProfileUpdateState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value = ProfileUpdateState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = ProfileUpdateState.Failure(ex.message)
                }
            }
        }
    }
    fun resetState() {
        _state.value = ProfileUpdateState.Initial
    }
}
