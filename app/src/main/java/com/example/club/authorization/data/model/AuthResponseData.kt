package com.example.club.authorization.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseData(
    /*  val success: Boolean,
    val reason: String?,
    val user: User?,*/
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken:String,
)
