package com.example.club.shared.event.domain.entity

data class Ticket(
    val id:String,
    val seat:String,
    val capacity:Int,
    val x:Float,
    val y:Float,
    val price:Int,
    val type:SeatType,
    val status:SeatStatus
)
