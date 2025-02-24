package com.example.club.authorization.domain.repository


import com.example.club.authorization.domain.AuthResponse

interface UserAuthRepository {
    suspend fun signIn(login: String, pass: String): AuthResponse
}