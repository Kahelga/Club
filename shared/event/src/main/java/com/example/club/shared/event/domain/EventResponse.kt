package com.example.club.shared.event.domain

import com.example.club.shared.event.domain.entity.Event

data class EventResponse (
    val success:Boolean,
    val reason: String?=null,
    val events: List<Event>
)


