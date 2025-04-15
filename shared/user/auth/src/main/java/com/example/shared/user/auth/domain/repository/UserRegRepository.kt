package com.example.shared.user.auth.domain.repository

import com.example.shared.user.auth.domain.entity.RegResponse

interface UserRegRepository {
    suspend fun register(name:String,email: String, pass: String): RegResponse
}