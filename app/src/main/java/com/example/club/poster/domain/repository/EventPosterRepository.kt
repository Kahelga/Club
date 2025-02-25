package com.example.club.poster.domain.repository

import com.example.club.poster.domain.EventResponse
import com.example.club.poster.domain.entity.Event

interface EventPosterRepository {
    suspend fun getAll(): List<Event>//EventResponse
}