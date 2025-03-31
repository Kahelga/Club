package com.example.club.profileUpdate.domain.usecase

import com.example.club.profile.domain.entity.User
import com.example.club.profileUpdate.domain.repository.ProfileUpdateRepository

class UpdateProfileUseCase(private val profileUpdateRepository: ProfileUpdateRepository) {
    suspend operator fun invoke(login:String,token:String,newUser:User): User =
        profileUpdateRepository.update(login,token,newUser)
}