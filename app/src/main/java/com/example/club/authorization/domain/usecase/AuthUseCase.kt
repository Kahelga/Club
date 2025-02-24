package com.example.club.authorization.domain.usecase


import com.example.club.authorization.domain.AuthResponse
import com.example.club.authorization.domain.repository.UserAuthRepository

class AuthUseCase(private val userAuthRepository: UserAuthRepository) {
    suspend operator fun invoke(login: String, pass: String): AuthResponse =
        userAuthRepository.signIn(login, pass)
}