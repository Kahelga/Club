package com.example.club.poster.domain

import com.example.club.poster.domain.entity.Event

data class EventResponse (
    val success:Boolean,
    val reason: String?=null,
    val events: List<Event>
)


