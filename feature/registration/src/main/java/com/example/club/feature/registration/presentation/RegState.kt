package com.example.club.feature.registration.presentation

import com.example.shared.user.auth.domain.entity.RegResponse

interface RegState {
    data object Initial : RegState
    data class Failure(val message: String?) : RegState
    data class Success(val response: RegResponse): RegState
}