package com.example.club.authorization.domain.entity

data class User(
    val phone: String,
    val firstname: String,
    val middlename: String,
    val lastname: String,
    val email: String,
    val city: String
)
