package com.example.club.shared.event.domain.entity

data class SeatPlan(
    val row:Int,
    val column:Int,
    val tickets:List<Ticket>
)
