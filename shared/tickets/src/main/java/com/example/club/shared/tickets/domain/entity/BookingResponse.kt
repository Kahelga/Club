package com.example.club.shared.tickets.domain.entity

data class BookingResponse(
    val bookedId:String,
    val status: PurchaseStatus
)
