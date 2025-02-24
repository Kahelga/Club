package com.example.club.eventDetails.domain.repository

import com.example.club.eventDetails.domain.entity.EventDetails

interface EventDetailsRepository {
    suspend fun get(eventId: String):EventDetails
}