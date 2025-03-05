package com.example.club.purchase.domain.entity

data class PurchaseRequest(
    val eventId:String,
    val seats:List<String>
)
