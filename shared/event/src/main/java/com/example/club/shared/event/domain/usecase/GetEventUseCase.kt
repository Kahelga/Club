package com.example.club.shared.event.domain.usecase


import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.shared.event.domain.repository.EventDetailsRepository

class GetEventUseCase(private val eventDetailsRepository: EventDetailsRepository) {
    suspend operator fun invoke(eventId:String): EventDetails =
        eventDetailsRepository.get(eventId)
}
