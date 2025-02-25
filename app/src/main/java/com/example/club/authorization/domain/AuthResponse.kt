package com.example.club.authorization.domain


data class AuthResponse(
  /*  val success: Boolean,
    val reason: String?,
    val user: User?,*/
    val accessToken: String?,
    val refreshToken:String
)
