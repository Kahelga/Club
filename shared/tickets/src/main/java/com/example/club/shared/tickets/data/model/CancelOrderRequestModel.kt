package com.example.club.shared.tickets.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CancelOrderRequestModel(
    val bookingId:String,
)
