package com.example.club.shared.event.domain


import com.example.club.shared.event.domain.entity.EventDetails

data class EventDetailsResponse(
    val success:Boolean,
    val reason: String?=null,
    val event: EventDetails
)
