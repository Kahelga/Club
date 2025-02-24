package com.example.club.eventDetails.data

import com.example.club.eventDetails.data.model.EventDetailsModel
import kotlinx.serialization.Serializable

@Serializable
data class EventDetailsResponseData(
    val success: Boolean,
    val reason: String? = null,
    val event: EventDetailsModel
)
