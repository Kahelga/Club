package com.example.club.eventDetails.domain.usecase

import com.example.club.eventDetails.domain.entity.EventDetails
import com.example.club.eventDetails.domain.repository.EventDetailsRepository

class GetEventUseCase(private val eventDetailsRepository: EventDetailsRepository) {
    suspend operator fun invoke(eventId:String): EventDetails=
        eventDetailsRepository.get(eventId)
}