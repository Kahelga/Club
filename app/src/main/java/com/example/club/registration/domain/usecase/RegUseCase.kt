package com.example.club.registration.domain.usecase

import com.example.club.registration.domain.entity.RegResponse
import com.example.club.registration.domain.repository.UserRegRepository

class RegUseCase(private val userRegRepository: UserRegRepository) {
suspend operator fun invoke(name:String,email: String, pass: String):RegResponse=
    userRegRepository.register(name,email,pass)
}