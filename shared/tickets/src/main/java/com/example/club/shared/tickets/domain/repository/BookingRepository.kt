package com.example.club.shared.tickets.domain.repository

import com.example.club.shared.tickets.domain.entity.BookingRequest
import com.example.club.shared.tickets.domain.entity.BookingResponse

interface BookingRepository {
    suspend fun bookedTicket(bookingRequest: BookingRequest,token:String):BookingResponse
}