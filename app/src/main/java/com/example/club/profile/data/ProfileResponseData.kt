package com.example.club.profile.data

import com.example.club.profile.data.model.UserModel
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponseData(
    val success: Boolean,
    val reason: String?,
    val user: UserModel

)
