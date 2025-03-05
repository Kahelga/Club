package com.example.club.hall.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SeatPlanModel(
    val row:Int,
    val column:Int,
    val tickets:List<TicketModel>
)
