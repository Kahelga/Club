package com.example.club.authorization.presentation

import androidx.lifecycle.ViewModel
import com.example.club.authorization.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AuthViewModel(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Initial)
    val state: StateFlow<AuthState> = _state
  /*  private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData*/
    private val _login=MutableStateFlow<String>("")
    val login:StateFlow<String> = _login

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
                _login.value=login
               // _userData.value = response.user
            } catch (ce: CancellationException) {
                throw ce
            }catch (e: Exception) {
                _state.value = AuthState.Failure(e.localizedMessage.orEmpty())
            }
        }
    }
}