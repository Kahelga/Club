package com.example.club.feature.booking.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.shared.tickets.domain.entity.BookingRequest
import com.example.club.shared.tickets.domain.usecase.BookingUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class BookingViewModel(
    private val bookingUseCase: BookingUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow<BookingState>(BookingState.Initial)
    val state: StateFlow<BookingState> = _state
    private var accessToken: String? = null

    fun bookedTicket(bookingRequest: BookingRequest){
        viewModelScope.launch {
            _state.value=BookingState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val response =bookingUseCase(bookingRequest,"Bearer $accessToken")
                _state.value=BookingState.Success(response)
            }catch (ce: CancellationException) {
            throw ce
        }catch (ex: Exception) {
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)

                            val response =bookingUseCase(bookingRequest,"Bearer $accessToken")
                            _state.value=BookingState.Success(response)
                        } else {
                            _state.value = BookingState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value =BookingState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = BookingState.Failure(ex.message)
                }
            }
        }
    }
}