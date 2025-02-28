package com.example.club.authorization.presentation

import androidx.lifecycle.ViewModel
import com.example.club.authorization.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.data.TokenManager
import com.example.club.eventDetails.EventDetailsRoute
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AuthViewModel(
    private val authUseCase: AuthUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Initial)
    val state: StateFlow<AuthState> = _state
    private val _login = MutableStateFlow<String>("")
    val login: StateFlow<String> = _login

    private var previousRoute: EventDetailsRoute? = null

    fun authorize(login: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authUseCase(login, password)
                /*if (response.success) {
                    _state.value = AuthState.Success(response)
                } else {
                    _state.value = AuthState.Failure(response.reason)
                }*/
                _state.value = AuthState.Success(response)
                _login.value = login
                response.accessToken?.let { tokenManager.saveTokens(it, response.refreshToken,response.expiresIn) }
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: Exception) {
                _state.value = AuthState.Failure(e.localizedMessage.orEmpty())
            }
        }
    }
    fun setPreviousRoute(route: EventDetailsRoute) {
        previousRoute = route
    }
    fun getPreviousRoute(): EventDetailsRoute? {
        return previousRoute
    }
    fun logout() {
        viewModelScope.launch {
            tokenManager.clearTokens()
            _state.value = AuthState.LoggedOut
        }
    }

}

