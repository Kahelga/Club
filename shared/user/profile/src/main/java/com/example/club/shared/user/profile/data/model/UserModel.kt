package com.example.club.shared.user.profile.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id:String,
    val phone: String,
    val lastname: String,
    val firstname: String,
    val middlename: String,
    val email: String,
    val city: String,
    val birthDate:String,
    val role:String
)