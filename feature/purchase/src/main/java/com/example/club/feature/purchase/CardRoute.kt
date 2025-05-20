package com.example.club.feature.purchase

import kotlinx.serialization.Serializable

@Serializable
data class CardRoute (
   // val eventId: String,
    //val seats: List<String>,
    val bookedId: String,
    val userId:String
)

