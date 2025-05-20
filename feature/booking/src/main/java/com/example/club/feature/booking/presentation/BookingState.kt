package com.example.club.feature.booking.presentation

import com.example.club.shared.tickets.domain.entity.BookingResponse

interface BookingState {
    data object Initial :BookingState
    data object Loading :BookingState
    data class Failure(val message: String?) :BookingState
    data class Success(val response:BookingResponse):BookingState
}