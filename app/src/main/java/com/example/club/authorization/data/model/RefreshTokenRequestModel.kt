package com.example.club.authorization.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestModel(
    @SerialName("refresh_token")
    val refreshToken:String,
)
