package com.example.club.shared.event.domain.repository

import com.example.club.shared.event.domain.entity.Event

interface EventPosterRepository {
    suspend fun getAll(): List<Event>//EventResponse
}