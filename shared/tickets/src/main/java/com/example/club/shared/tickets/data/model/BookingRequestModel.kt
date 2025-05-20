package com.example.club.shared.tickets.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BookingRequestModel(
    val eventId:String,
    val seats:List<String>,
    val totalPrice:Int,
    val login: String //userId
)
