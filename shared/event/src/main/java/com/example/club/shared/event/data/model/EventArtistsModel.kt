package com.example.club.shared.event.data.model

import com.example.club.shared.event.domain.entity.Professions
import kotlinx.serialization.Serializable

@Serializable
data class EventArtistsModel(
    val id:String,
    val name:String,
    val profession: Professions,
)
