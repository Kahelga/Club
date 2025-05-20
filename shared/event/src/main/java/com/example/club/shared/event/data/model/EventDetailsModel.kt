package com.example.club.shared.event.data.model

import com.example.club.shared.event.domain.entity.AgeRatings
import com.example.club.shared.event.domain.entity.EventArtists
import com.example.club.shared.event.domain.entity.EventStatus
import kotlinx.serialization.Serializable

@Serializable
data class EventDetailsModel(
    val id:String,
    val title:String,
    val date:String,
    val genre:List<String>,
    val ageRating: AgeRatings,
    val minPrice:Int,
    val imgPreview:String,
    val status: EventStatus,
    val description:String,
    val artists:List<EventArtistsModel>,
    val duration:Int
)
