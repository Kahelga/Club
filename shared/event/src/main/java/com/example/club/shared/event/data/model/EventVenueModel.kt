package com.example.club.shared.event.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EventVenueModel(
    val name:String,
    val capacity:Int
)
