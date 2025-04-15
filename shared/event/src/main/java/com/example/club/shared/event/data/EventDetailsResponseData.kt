package com.example.club.shared.event.data

import com.example.club.shared.event.data.model.EventDetailsModel
import kotlinx.serialization.Serializable

@Serializable
data class EventDetailsResponseData(
    val success: Boolean,
    val reason: String? = null,
    val event: EventDetailsModel
)
