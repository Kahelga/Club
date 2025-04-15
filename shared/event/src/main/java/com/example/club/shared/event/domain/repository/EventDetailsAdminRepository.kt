package com.example.club.shared.event.domain.repository

import com.example.club.shared.event.domain.entity.EventDetails

interface EventDetailsAdminRepository {
    suspend fun get(token:String,eventId: String): EventDetails
}