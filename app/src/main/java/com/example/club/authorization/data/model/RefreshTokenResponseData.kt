package com.example.club.authorization.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponseData(
    @SerialName("access_token")
    val accessToken:String
)
