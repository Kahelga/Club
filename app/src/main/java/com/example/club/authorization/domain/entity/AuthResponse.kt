package com.example.club.authorization.domain.entity


data class AuthResponse(
    val accessToken: String?,
    val refreshToken:String,

)
