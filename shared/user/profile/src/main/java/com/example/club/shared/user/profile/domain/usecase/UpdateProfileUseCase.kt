package com.example.club.shared.user.profile.domain.usecase

import com.example.club.shared.user.profile.domain.entity.User
import com.example.club.shared.user.profile.domain.repository.ProfileUpdateRepository

class UpdateProfileUseCase(private val profileUpdateRepository: ProfileUpdateRepository) {
    suspend operator fun invoke(login:String,token:String,newUser: User): User =
        profileUpdateRepository.update(login,token,newUser)
}