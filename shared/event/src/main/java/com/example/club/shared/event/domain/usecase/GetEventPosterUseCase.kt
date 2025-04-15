package com.example.club.shared.event.domain.usecase


import com.example.club.shared.event.domain.entity.Event
import com.example.club.shared.event.domain.repository.EventPosterRepository

class GetEventPosterUseCase(private val repository: EventPosterRepository) {
    suspend operator fun invoke(): List<Event> = repository.getAll()
}

