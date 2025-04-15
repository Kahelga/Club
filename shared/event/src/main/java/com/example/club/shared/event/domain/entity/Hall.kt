package com.example.club.shared.event.domain.entity

data class Hall(
    val id:String,
    val name:String,
    val capacity:Int,
    val seatingPlan:SeatPlan
)
