package com.example.club.purchase.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.data.TokenManager
import com.example.club.authorization.domain.usecase.RefreshTokenUseCase
import com.example.club.purchase.domain.entity.PurchaseRequest
import com.example.club.purchase.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class PurchaseViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow<PurchaseState>(PurchaseState.Initial)
    val state: StateFlow<PurchaseState> = _state
    private var accessToken: String? = null
    fun buyTicket(purchaseRequest: PurchaseRequest) {
        viewModelScope.launch {
            _state.value = PurchaseState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val response =purchaseUseCase(purchaseRequest,"Bearer $accessToken")
                _state.value =PurchaseState.Success(response)
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

                            val response =purchaseUseCase(purchaseRequest,"Bearer $accessToken")
                            _state.value =PurchaseState.Success(response)
                        } else {
                            _state.value = PurchaseState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value = PurchaseState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = PurchaseState.Failure(ex.message)
                }
            }
        }
    }

}