package com.example.club.eventDetails.data.model


import kotlinx.serialization.Serializable

@Serializable
data class EventDetailsModel(
    val id: String,
    val name: String,
    val description: String,
    val date: String,
    val time: String,
    val artists: List<EventArtistsModel>,
    val venue: EventVenueModel,
    val duration: Int,
    val ageRating: String,
    val genres: List<String>,
    val img: String,
    val minPrice: Int
)
