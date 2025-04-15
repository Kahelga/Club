package com.example.club.shared.event.domain.usecase

import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.shared.event.domain.repository.EventsAdminRepository

class GetEventsAdminUseCase (private val repository: EventsAdminRepository) {
    suspend operator fun invoke(token:String): List<EventDetails> = repository.getAll(token)
}
