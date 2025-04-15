package com.example.club.shared.tickets.domain.entity

data class PurchaseResponse(
    val ticketId:String,
    val ticketCode:String,
    val status: PurchaseStatus
)
