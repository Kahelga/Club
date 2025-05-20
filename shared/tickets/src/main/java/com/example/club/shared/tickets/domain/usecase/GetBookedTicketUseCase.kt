package com.example.club.shared.tickets.domain.usecase

import com.example.club.shared.tickets.domain.entity.BookedTicket
import com.example.club.shared.tickets.domain.repository.BookedTicketRepository

class GetBookedTicketUseCase(private val bookedTicketRepository: BookedTicketRepository) {
    suspend operator fun invoke(bookedId:String,token:String): BookedTicket =
        bookedTicketRepository.getBookedTicket(bookedId,token)
}