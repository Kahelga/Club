package com.example.club.feature.admin.events.presentation

import com.example.club.shared.event.domain.entity.EventDetails


interface EventsState {
    data object Initial : EventsState
    data object Loading : EventsState
    data class Failure(val message: String?) : EventsState
    data class Content(val events: List<EventDetails>) : EventsState
}