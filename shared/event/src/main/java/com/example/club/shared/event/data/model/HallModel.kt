package com.example.club.shared.event.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HallModel(
    val id:String,
    val name:String,
    val capacity:Int,
    val seatingPlan: SeatPlanModel
)
