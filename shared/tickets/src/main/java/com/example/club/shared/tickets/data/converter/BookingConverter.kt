package com.example.club.shared.tickets.data.converter

import com.example.club.shared.event.data.converter.EventPosterConverter
import com.example.club.shared.tickets.data.model.BookedTicketModel
import com.example.club.shared.tickets.data.model.BookingResponseData
import com.example.club.shared.tickets.domain.entity.BookedTicket
import com.example.club.shared.tickets.domain.entity.BookingResponse

class BookingConverter {
    private val eventPosterConverter= EventPosterConverter()
    fun convert(bookingResponseData: BookingResponseData):BookingResponse{
        return BookingResponse(
            bookedId = bookingResponseData.bookedId,
            status = bookingResponseData.status
        )
    }
    fun convertBookedTicket(bookedTicketModel: BookedTicketModel):BookedTicket{
        return BookedTicket(
            bookingId = bookedTicketModel.bookingId,
            userId = bookedTicketModel.userId,
            event = eventPosterConverter.convert(bookedTicketModel.event),
            seats = bookedTicketModel.seats,
            totalPrice = bookedTicketModel.totalPrice,
            issueDate = bookedTicketModel.issueDate,
            status = bookedTicketModel.status
        )
    }
}