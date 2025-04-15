package com.example.club.feature.purchase

import kotlinx.serialization.Serializable

@Serializable
data class DataRoute(
    val eventId: String,
    val seats: List<String>
)
