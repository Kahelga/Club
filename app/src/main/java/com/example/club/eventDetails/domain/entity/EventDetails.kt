package com.example.club.eventDetails.domain.entity

data class EventDetails(
    val id:String,
    val name:String,
    val description:String,
    val date:String,
    val time:String,
    val artists:List<EventArtists>,
    val venue:EventVenue,
    val duration:Int,
    val ageRating:String,
    val genres:List<String>,
    val img:String,
    val minPrice:Int

)
