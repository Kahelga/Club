package com.example.club.profile.domain

import com.example.club.authorization.domain.entity.User

data class ProfileResponse(
    val success: Boolean,
    val reason: String?,
    val user: User
)
