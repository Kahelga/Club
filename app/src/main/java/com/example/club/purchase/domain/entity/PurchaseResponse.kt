package com.example.club.purchase.domain.entity

data class PurchaseResponse(
    val ticketId:String,
    val ticketCode:String,
    val status: PurchaseStatus
)
