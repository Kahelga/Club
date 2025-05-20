package com.example.shared.user.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseData(
    val accessToken: String,
    val refreshToken:String,
    val message:String,
    val role:String
)
