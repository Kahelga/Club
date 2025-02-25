package com.example.club.eventDetails.domain.entity

import com.example.club.poster.domain.entity.AgeRatings
import com.example.club.poster.domain.entity.EventStatus

data class EventDetails(
    val id:String,
    val title:String,
    val date:String,
    val genre:List<String>,
    val ageRating: AgeRatings,
    val description:String,
    val artists:List<EventArtists>,
    val duration:Int,
    val imgPreview:String,
    val minPrice:Int,
    val status:EventStatus

)
