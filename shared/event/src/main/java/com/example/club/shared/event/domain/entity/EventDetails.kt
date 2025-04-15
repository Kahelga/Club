package com.example.club.shared.event.domain.entity

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
    val status: EventStatus

)
