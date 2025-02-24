package com.example.club.authorization.presentation

import androidx.lifecycle.ViewModel
import com.example.club.authorization.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.domain.entity.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AuthViewModel(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Initial)
    val state: StateFlow<AuthState> = _state
    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData

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
                _userData.value = response.user
            } catch (ce: CancellationException) {
                throw ce
            }catch (e: Exception) {
                _state.value = AuthState.Failure(e.localizedMessage.orEmpty())
            }
        }
    }
}