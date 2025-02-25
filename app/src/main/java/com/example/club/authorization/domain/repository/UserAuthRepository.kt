package com.example.club.authorization.domain.repository


import com.example.club.authorization.domain.AuthResponse

interface UserAuthRepository {
    suspend fun signIn(email: String, pass: String): AuthResponse
}