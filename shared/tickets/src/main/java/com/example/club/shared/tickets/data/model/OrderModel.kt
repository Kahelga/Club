package com.example.club.shared.tickets.data.model

import com.example.club.shared.event.data.model.EventModel
import com.example.club.shared.tickets.domain.entity.PurchaseStatus
import kotlinx.serialization.Serializable

@Serializable
data class OrderModel(
    val ticketId:String,
    val bookingId:String,
    val userId:String,
    val event: EventModel,
    val seats:List<String>,
    val ticketCode:String,
    val issueDate:String,
    val status: PurchaseStatus
)
