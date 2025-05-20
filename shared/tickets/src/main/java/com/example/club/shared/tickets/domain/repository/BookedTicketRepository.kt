package com.example.club.shared.tickets.domain.repository

import com.example.club.shared.tickets.domain.entity.BookedTicket

interface BookedTicketRepository {
    suspend fun getBookedTicket(bookedId:String,token:String): BookedTicket
}