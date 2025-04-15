package com.example.shared.user.auth.domain.repository


import com.example.shared.user.auth.domain.entity.AuthResponse

interface UserAuthRepository {
    suspend fun signIn(email: String, pass: String): AuthResponse
}