package com.example.club.shared.tickets.domain.entity

data class PurchaseRequest(
    val eventId:String,
    val seats:List<String>
)
