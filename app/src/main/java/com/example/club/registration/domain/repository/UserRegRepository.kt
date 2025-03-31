package com.example.club.registration.domain.repository

import com.example.club.registration.domain.entity.RegResponse

interface UserRegRepository {
    suspend fun register(name:String,email: String, pass: String):RegResponse
}