package com.example.club.feature.booking.presentation

import com.example.club.shared.tickets.domain.entity.BookedTicket

interface BookedTicketState {
    data object Initial :BookedTicketState
    data object Loading :BookedTicketState
    data class Failure(val message: String?) :BookedTicketState
    data class Content(val bookedTicket:BookedTicket):BookedTicketState

}