package com.example.club.shared.event.data.model

import com.example.club.shared.event.domain.entity.AgeRatings
import com.example.club.shared.event.domain.entity.EventStatus
import kotlinx.serialization.Serializable

@Serializable
data class EventModel(
    val id:String,
    val title:String,
    val date:String,
    val genre:List<String>,
    val ageRating: AgeRatings,
    val minPrice:Int, //double
    val imgPreview:String,
    val status: EventStatus
)
