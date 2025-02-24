package com.example.club.eventDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.eventDetails.domain.usecase.GetEventUseCase

class EventDetailsViewModelFactory(
    private val eventId: String,
    private val getEventUseCase: GetEventUseCase,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == EventDetailsViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return EventDetailsViewModel(eventId, getEventUseCase) as T
    }
}