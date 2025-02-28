package com.example.club.authorization.domain.entity


data class AuthResponse(
    val accessToken: String?,
    val refreshToken:String,
    val expiresIn:Long
)
/*  val success: Boolean,
    val reason: String?,
    val user: User?,*/