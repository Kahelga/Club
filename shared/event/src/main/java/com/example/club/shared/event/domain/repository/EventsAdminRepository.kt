package com.example.club.shared.event.domain.repository

import com.example.club.shared.event.domain.entity.EventDetails

interface EventsAdminRepository {
    suspend fun getAll(token:String): List<EventDetails>
}