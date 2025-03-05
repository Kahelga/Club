package com.example.club.hall.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.hall.domain.usecase.GetHallUseCase
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
                // val user = getProfileUseCase(login,"Bearer $accessToken")
                if (accessToken == null || tokenManager.isAccessTokenExpired()) {//user.massage=="token is unactive"
                    val refreshToken = tokenManager.getRefreshToken()
                    if (refreshToken != null) {
                        val newToken=refreshTokenUseCase(refreshToken)
                        accessToken =newToken.accessToken
                        tokenManager.updateAccessToken(accessToken)
                    }
                }
                val hall =getHallUseCase(eventId,"Bearer $accessToken")
                _state.value =HallState.Content(hall)
            }catch (ce: CancellationException){
                throw ce
            }catch (ex: Exception){
                _state.value =HallState.Failure(ex.message)
            }
        }
    }
}