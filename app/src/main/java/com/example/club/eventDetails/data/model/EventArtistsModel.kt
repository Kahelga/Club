package com.example.club.eventDetails.data.model

import com.example.club.eventDetails.domain.entity.Professions
import kotlinx.serialization.Serializable

@Serializable
data class EventArtistsModel(
    val id:String,
    val name:String,
    val profession:Professions,
)
