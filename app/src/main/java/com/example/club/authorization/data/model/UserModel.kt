package com.example.club.authorization.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val phone: String,
    val firstname: String,
    val middlename: String,
    val lastname: String,
    val email: String,
    val city: String
)
