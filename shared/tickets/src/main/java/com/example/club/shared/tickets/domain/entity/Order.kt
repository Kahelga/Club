package com.example.club.shared.tickets.domain.entity

import com.example.club.shared.event.domain.entity.Event


data class Order(
    val ticketId:String,
    val userId:String,
    val event: Event,
    val seats:List<String>,
    val issueDate:String,
    val status:PurchaseStatus
)
