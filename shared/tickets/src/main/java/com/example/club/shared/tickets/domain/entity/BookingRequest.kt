package com.example.club.shared.tickets.domain.entity

data class BookingRequest(
 val eventId:String,
 val seats:List<String>,
 val totalPrice:Int,
 val login: String //userId
)
