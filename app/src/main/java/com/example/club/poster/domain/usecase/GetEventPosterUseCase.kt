package com.example.club.poster.domain.usecase

import com.example.club.poster.domain.EventResponse
import com.example.club.poster.domain.entity.Event
import com.example.club.poster.domain.repository.EventPosterRepository

class GetEventPosterUseCase(private val repository: EventPosterRepository) {
    suspend operator fun invoke(): /*EventResponse*/List<Event> = repository.getAll()
}