package com.example.club.feature.auth.presentation

import androidx.lifecycle.ViewModel
import com.example.shared.user.auth.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.example.club.feature.eventdetails.EventDetailsRoute
import com.example.club.util.manager.token.TokenManager

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
                response.accessToken?.let { tokenManager.saveTokens(it, response.refreshToken) }
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

