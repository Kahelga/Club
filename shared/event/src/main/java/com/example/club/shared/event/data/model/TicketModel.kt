package com.example.club.shared.event.data.model


import com.example.club.shared.event.domain.entity.SeatStatus
import com.example.club.shared.event.domain.entity.SeatType
import kotlinx.serialization.Serializable

@Serializable
data class TicketModel(
    val id:String,
    val x:Float,
    val y:Float,
    val seat:String,
    val capacity:Int,
    val price:Int,
    val type: SeatType,
    val status: SeatStatus
)
