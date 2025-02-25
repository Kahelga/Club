package com.example.club.poster.presentation

import com.example.club.poster.domain.EventResponse
import com.example.club.poster.domain.entity.Event

sealed interface PosterState {
    data object Initial:PosterState
    data object Loading:PosterState
    data class Failure(val message: String?) : PosterState
    data class Content(val events:/* EventResponse*/List<Event>) : PosterState
}