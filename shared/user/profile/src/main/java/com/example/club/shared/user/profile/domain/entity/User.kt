package com.example.club.shared.user.profile.domain.entity

data class User(
    val id:String,
    val phone: String,
    val lastname: String,
    val firstname: String,
    val middlename: String,
    val email: String,
    val city: String,
    val role:String
)

