package com.example.club.tickets.domain.entity

import com.example.club.poster.domain.entity.Event
import com.example.club.purchase.domain.entity.PurchaseStatus

data class Order(
    val ticketId:String,
    val userId:String,
    val event: Event,
    val seats:List<String>,
    val issueDate:String,
    val status:PurchaseStatus
)
