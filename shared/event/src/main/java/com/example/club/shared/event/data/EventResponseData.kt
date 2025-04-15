package com.example.club.shared.event.data

import com.example.club.shared.event.data.model.EventModel

import kotlinx.serialization.Serializable

@Serializable
data class EventResponseData(
    val success: Boolean,
    val reason: String? = null,
    val events: List<EventModel>
)
