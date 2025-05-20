package com.example.club.shared.tickets.data.repository

import com.example.club.shared.tickets.data.converter.BookingConverter
import com.example.club.shared.tickets.data.model.BookingRequestModel
import com.example.club.shared.tickets.data.network.BookingApi
import com.example.club.shared.tickets.domain.entity.BookingRequest
import com.example.club.shared.tickets.domain.entity.BookingResponse
import com.example.club.shared.tickets.domain.repository.BookingRepository

class BookingRepositoryImpl(
    private val bookingApi: BookingApi,
    private val bookingConverter: BookingConverter
):BookingRepository {
    override suspend fun bookedTicket(
        bookingRequest: BookingRequest,
        token: String
    ): BookingResponse {
        val request=BookingRequestModel(bookingRequest.eventId,bookingRequest.seats,bookingRequest.totalPrice,bookingRequest.login)
        val response=bookingApi.bookedTicket(request,token)
        return bookingConverter.convert(response)
    }

}