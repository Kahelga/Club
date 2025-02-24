package com.example.club.authorization.data

import com.example.club.authorization.data.model.UserModel
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseData(
    val success: Boolean,
    val reason: String?,
    val user: UserModel?,
    val token: String?
)
