package com.example.shared.user.auth.domain.entity

data class RegRequest(
    val name:String,
    val email: String,
    val password: String
)
