package com.example.club.registration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegRequestModel(
    val name:String,
    val email: String,
    val password: String
)
