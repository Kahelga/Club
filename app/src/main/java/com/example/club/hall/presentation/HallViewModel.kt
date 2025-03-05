package com.example.club.hall.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.hall.domain.usecase.GetHallUseCase
import com.example.club.profile.presentation.ProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class HallViewModel(
    private val eventId: String,
    private val getHallUseCase: GetHallUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow<HallState>(HallState.Initial)
    val state: StateFlow<HallState> = _state
    private var accessToken: String? = null
    fun loadHall(){
        viewModelScope.launch {
            _state.value =HallState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val hall =getHallUseCase(eventId,"Bearer $accessToken")
                _state.value =HallState.Content(hall)
            }catch (ce: CancellationException){
                throw ce
            }catch (ex: Exception){
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)

                            val hall =getHallUseCase(eventId,"Bearer $accessToken")
                            _state.value = HallState.Content(hall)
                        } else {
                            _state.value = HallState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value = HallState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = HallState.Failure(ex.message)
                }
            }
        }
    }
}