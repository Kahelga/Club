package com.example.club.hall.data.model

import kotlinx.serialization.Serializable

@Serializable
data class HallModel(
    val id:String,
    val name:String,
    val capacity:Int,
    val seatingPlan: SeatPlanModel
)
