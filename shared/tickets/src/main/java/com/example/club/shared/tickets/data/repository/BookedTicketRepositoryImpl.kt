package com.example.club.shared.tickets.data.repository

import com.example.club.shared.tickets.data.converter.BookingConverter
import com.example.club.shared.tickets.data.network.BookingDetailsApi
import com.example.club.shared.tickets.domain.entity.BookedTicket
import com.example.club.shared.tickets.domain.repository.BookedTicketRepository

class BookedTicketRepositoryImpl(
    private val bookingDetailsApi: BookingDetailsApi,
    private val bookingConverter: BookingConverter
):BookedTicketRepository {
    override suspend fun getBookedTicket(bookedId: String, token: String): BookedTicket {
        val model=bookingDetailsApi.bookedTicket(bookedId,token)
        return bookingConverter.convertBookedTicket(model)
    }
}