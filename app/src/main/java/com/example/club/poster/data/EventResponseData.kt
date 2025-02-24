package com.example.club.poster.data

import com.example.club.poster.data.model.EventModel

import kotlinx.serialization.Serializable

@Serializable
data class EventResponseData(
    val success: Boolean,
    val reason: String? = null,
    val events: List<EventModel>
)
