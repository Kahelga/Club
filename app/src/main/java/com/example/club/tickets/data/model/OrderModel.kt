package com.example.club.tickets.data.model

import com.example.club.poster.data.model.EventModel
import com.example.club.poster.domain.entity.Event
import com.example.club.purchase.domain.entity.PurchaseStatus
import kotlinx.serialization.Serializable

@Serializable
data class OrderModel(
    val ticketId:String,
    val userId:String,
    val event: EventModel,
    val seats:List<String>,
    val issueDate:String,
    val status: PurchaseStatus
)
