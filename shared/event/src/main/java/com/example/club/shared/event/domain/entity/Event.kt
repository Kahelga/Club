package com.example.club.shared.event.domain.entity

data class Event (
    val id:String,
    val title:String,
    val date:String,
    val genre:List<String>,
    val ageRating: AgeRatings,
    val minPrice:Int, //double
    val imgPreview:String,
    val status: EventStatus
)

