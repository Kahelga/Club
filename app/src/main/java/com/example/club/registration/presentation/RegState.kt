package com.example.club.registration.presentation

import com.example.club.registration.domain.entity.RegResponse

interface RegState {
    data object Initial :RegState
    data class Failure(val message: String?) :RegState
    data class Success(val response:RegResponse):RegState
}