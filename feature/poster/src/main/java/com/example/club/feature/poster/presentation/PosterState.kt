package com.example.club.feature.poster.presentation

import com.example.club.shared.event.domain.entity.Event


sealed interface PosterState {
    data object Initial: PosterState
    data object Loading: PosterState
    data class Failure(val message: String?) : PosterState
    data class Content(val events:List<Event>) : PosterState
}