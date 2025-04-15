package com.example.club.shared.event.domain.usecase

import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.shared.event.domain.repository.EventDetailsAdminRepository


class GetEventAdminUseCase(private val eventDetailsAdminRepository: EventDetailsAdminRepository) {
    suspend operator fun invoke(token:String,eventId:String): EventDetails =
        eventDetailsAdminRepository.get(token,eventId)
}