package com.example.club.shared.tickets.domain.usecase

import com.example.club.shared.tickets.domain.entity.BookingRequest
import com.example.club.shared.tickets.domain.entity.BookingResponse
import com.example.club.shared.tickets.domain.repository.BookingRepository

class BookingUseCase(private val bookingRepository: BookingRepository) {
    suspend operator fun invoke(bookingRequest: BookingRequest,token:String):BookingResponse=
        bookingRepository.bookedTicket(bookingRequest,token)
}