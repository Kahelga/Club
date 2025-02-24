package com.example.club.eventDetails.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EventVenueModel(
    val name:String,
    val capacity:Int
)
