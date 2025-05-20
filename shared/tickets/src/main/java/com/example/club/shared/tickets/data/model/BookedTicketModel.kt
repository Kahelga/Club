package com.example.club.shared.tickets.data.model

import com.example.club.shared.event.data.model.EventModel
import com.example.club.shared.event.domain.entity.Event
import com.example.club.shared.tickets.domain.entity.PurchaseStatus
import kotlinx.serialization.Serializable

@Serializable
data class BookedTicketModel(
    val bookingId:String,
    val userId:String,
    val event: EventModel,
    val seats:List<String>,
    val totalPrice:Int,
    val issueDate:String,
    val status: PurchaseStatus
)
