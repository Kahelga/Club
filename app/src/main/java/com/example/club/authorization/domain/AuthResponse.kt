package com.example.club.authorization.domain

import com.example.club.authorization.domain.entity.User

data class AuthResponse(
    val success: Boolean,
    val reason: String?,
    val user: User?,
    val token: String?
)
