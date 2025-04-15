package com.example.club.feature.eventdetails.presentation

import com.example.club.shared.event.domain.entity.EventDetails


interface EventDetailsState {
    data object Initial : EventDetailsState
    data object Loading : EventDetailsState
    data class Failure(val message: String?) : EventDetailsState
    data class Content(val event: EventDetails) : EventDetailsState
}