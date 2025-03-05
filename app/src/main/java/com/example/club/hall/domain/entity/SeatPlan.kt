package com.example.club.hall.domain.entity

data class SeatPlan(
    val row:Int,
    val column:Int,
    val tickets:List<Ticket>
)
