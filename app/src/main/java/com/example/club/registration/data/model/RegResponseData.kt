package com.example.club.registration.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegResponseData(
    val userId:String,
    val message:String
)
