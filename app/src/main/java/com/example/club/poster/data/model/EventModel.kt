package com.example.club.poster.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EventModel(
    val id:String,
    val name:String,
    val date:String,
    val genres:List<String>,
    val ageRating:String,
    val minPrice:Int,
    val img:String
)
