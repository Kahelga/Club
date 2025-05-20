package com.example.club.feature.booking.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.shared.tickets.domain.usecase.GetBookedTicketUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class BookedTicketViewModel(
    private val bookedId:String,
    private val getBookedTicketUseCase: GetBookedTicketUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager
):ViewModel() {
    private val _state= MutableStateFlow <BookedTicketState>(BookedTicketState.Initial)
    val state: StateFlow<BookedTicketState> = _state
    private var accessToken: String? = null

    fun loadBookedTicket(){
        viewModelScope.launch {
            _state.value=BookedTicketState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val model=getBookedTicketUseCase(bookedId,"Bearer $accessToken")
                _state.value=BookedTicketState.Content(model)
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

                            val model=getBookedTicketUseCase(bookedId,"Bearer $accessToken")
                            _state.value=BookedTicketState.Content(model)
                        } else {
                            _state.value = BookedTicketState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value =BookedTicketState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = BookedTicketState.Failure(ex.message)
                }
            }

        }
    }
}