package com.example.club.hall.data.model

import com.example.club.hall.domain.entity.SeatStatus
import com.example.club.hall.domain.entity.SeatType
import kotlinx.serialization.Serializable

@Serializable
data class TicketModel(
    val id:String,
    val x:Float,
    val y:Float,
    val seat:String,
    val price:Int,
    val type: SeatType,
    val status: SeatStatus
)
