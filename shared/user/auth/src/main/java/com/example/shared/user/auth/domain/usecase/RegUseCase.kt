package com.example.shared.user.auth.domain.usecase

import com.example.shared.user.auth.domain.entity.RegResponse
import com.example.shared.user.auth.domain.repository.UserRegRepository

class RegUseCase(private val userRegRepository: UserRegRepository) {
suspend operator fun invoke(name:String,email: String, pass: String): RegResponse =
    userRegRepository.register(name,email,pass)
}