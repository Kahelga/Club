package com.example.club.authorization.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequestModel(
    val email: String,
    val password: String
)
