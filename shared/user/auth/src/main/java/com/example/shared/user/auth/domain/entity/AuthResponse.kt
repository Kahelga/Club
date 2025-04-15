package com.example.shared.user.auth.domain.entity


data class AuthResponse(
    val accessToken: String?,
    val refreshToken:String,

)
