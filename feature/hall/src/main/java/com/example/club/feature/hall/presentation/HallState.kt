package com.example.club.feature.hall.presentation

import com.example.club.shared.event.domain.entity.Hall


interface HallState {
    data object Initial : HallState
    data object Loading : HallState
    data class Failure(val message: String?) : HallState
    data class Content(val hall: Hall): HallState
}