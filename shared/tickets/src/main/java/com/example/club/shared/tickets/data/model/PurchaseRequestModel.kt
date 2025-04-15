package com.example.club.shared.tickets.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequestModel(
    val eventId:String,
    val seats:List<String>
)
