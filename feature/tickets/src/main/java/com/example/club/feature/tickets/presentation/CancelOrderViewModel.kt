package com.example.club.feature.tickets.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.shared.tickets.domain.entity.CancelOrderRequest
import com.example.club.shared.tickets.domain.entity.PurchaseRequest
import com.example.club.shared.tickets.domain.usecase.CancelOrderUseCase
import com.example.club.shared.tickets.domain.usecase.PurchaseUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CancelOrderViewModel(
   // private val bookingId: String,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow<CancelOrderState>(CancelOrderState.Initial)
    val state: StateFlow<CancelOrderState> = _state
    private var accessToken: String? = null

    fun cancelTicket(cancelOrderRequest: CancelOrderRequest) {
        viewModelScope.launch {
            _state.value = CancelOrderState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val response =cancelOrderUseCase(/*bookingId*/cancelOrderRequest,"Bearer $accessToken")
                _state.value = CancelOrderState.Success(response)
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

                            val response =cancelOrderUseCase(/*bookingId*/cancelOrderRequest,"Bearer $accessToken")
                            _state.value = CancelOrderState.Success(response)
                        } else {
                            _state.value = CancelOrderState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value = CancelOrderState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = CancelOrderState.Failure(ex.message)
                }
            }
        }
    }
}