package com.example.shared.user.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegRequestModel(
    val name:String,
    val email: String,
    val password: String
)
