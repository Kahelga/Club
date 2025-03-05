package com.example.club.purchase

import com.example.club.hall.data.model.TicketModel
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRoute (
    val eventId:String,
    val seats:List<String>,
    val totalPrice:Int
)