package com.example.club.profile.data.model

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
    val role:String
)