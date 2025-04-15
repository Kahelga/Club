package com.example.shared.user.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestModel(
    @SerialName("refresh_token")
    val refreshToken:String,
)
