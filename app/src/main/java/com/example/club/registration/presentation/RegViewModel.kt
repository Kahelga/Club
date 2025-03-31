package com.example.club.registration.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.authorization.presentation.AuthState
import com.example.club.registration.domain.usecase.RegUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class RegViewModel(
    private val regUseCase: RegUseCase
):ViewModel() {
    private val _state = MutableStateFlow<RegState>(RegState.Initial)
    val state: StateFlow<RegState> = _state

    fun register(name:String,email: String, pass: String){
        viewModelScope.launch {
            try {
                val response=regUseCase(name,email,pass)
                _state.value =RegState.Success(response)
            }catch (ce: CancellationException){
                throw ce
            }catch (e: Exception){
                _state.value=RegState.Failure(e.localizedMessage.orEmpty())
            }
        }
    }

}