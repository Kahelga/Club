package com.example.shared.user.auth.data.model

import kotlinx.serialization.Serializable


@Serializable
data class AuthRequestModel(
    val email: String,
    val password: String
)
