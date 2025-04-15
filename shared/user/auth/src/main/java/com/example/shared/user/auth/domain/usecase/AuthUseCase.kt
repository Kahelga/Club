package com.example.shared.user.auth.domain.usecase


import com.example.shared.user.auth.domain.entity.AuthResponse
import com.example.shared.user.auth.domain.repository.UserAuthRepository

class AuthUseCase(private val userAuthRepository: UserAuthRepository) {
    suspend operator fun invoke(email: String, pass: String): AuthResponse =
        userAuthRepository.signIn(email, pass)
}