package com.example.club.authorization.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequestModel(
    val login: String,
    val pass: String
)
