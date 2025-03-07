package com.example.club.tickets.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.profile.presentation.ProfileState
import com.example.club.tickets.domain.usecase.GetOrderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel (
    private val getOrderUseCase: GetOrderUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
):ViewModel(){
    private val _state = MutableStateFlow<OrderState>(OrderState.Initial)
    val state: StateFlow<OrderState> = _state
    private var accessToken: String? = null

    fun loadOrder(){
        viewModelScope.launch {
            _state.value = OrderState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val orders = getOrderUseCase( "Bearer $accessToken")
                _state.value =OrderState.Content(orders)

            } catch (ex: Exception) {
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)

                            val orders = getOrderUseCase( "Bearer $accessToken")
                            _state.value =OrderState.Content(orders)
                        } else {
                            _state.value = OrderState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value = OrderState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = OrderState.Failure(ex.message)
                }
            }
        }
    }
}