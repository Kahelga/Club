package com.example.club.poster.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.poster.domain.usecase.GetEventPosterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException

class PosterViewModel(private val getEventPosterUseCase: GetEventPosterUseCase) : ViewModel() {
    private val _state = MutableStateFlow<PosterState>(PosterState.Initial)
    val state: StateFlow<PosterState> = _state

    fun loadEvents() {
        viewModelScope.launch {
            _state.value = PosterState.Loading
            try {
                val events = getEventPosterUseCase()
                _state.value = PosterState.Content(events)

            } catch (ce: CancellationException) {
                throw ce
            } catch (ex: Exception) {
                _state.value = PosterState.Failure(ex.localizedMessage.orEmpty())
            }
        }
    }
}
