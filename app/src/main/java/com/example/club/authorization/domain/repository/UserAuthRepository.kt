package com.example.club.authorization.domain.repository


import com.example.club.authorization.domain.entity.AuthResponse

interface UserAuthRepository {
    suspend fun signIn(email: String, pass: String): AuthResponse
}