package com.example.club.eventDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.eventDetails.domain.usecase.GetEventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class EventDetailsViewModel(
    private val eventId: String,
    private val getEventUseCase: GetEventUseCase,

    ) : ViewModel() {
    private val _state = MutableStateFlow<EventDetailsState>(EventDetailsState.Initial)
    val state: StateFlow<EventDetailsState> = _state

    fun loadEvent() {
        viewModelScope.launch {
            _state.value = EventDetailsState.Loading
            try {
                val event = getEventUseCase(eventId)
                _state.value = EventDetailsState.Content(event)
            } catch (ce: CancellationException) {
                throw ce
            } catch (ex: Exception) {
                _state.value = EventDetailsState.Failure(ex.message)
            }
        }
    }
}