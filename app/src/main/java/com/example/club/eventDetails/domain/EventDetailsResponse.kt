package com.example.club.eventDetails.domain

import com.example.club.eventDetails.domain.entity.EventDetails

data class EventDetailsResponse(
    val success:Boolean,
    val reason: String?=null,
    val event: EventDetails
)
