package com.example.club.shared.event.domain.repository

import com.example.club.shared.event.domain.entity.EventDetails


interface EventDetailsRepository {
    suspend fun get(eventId: String): EventDetails
}