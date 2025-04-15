package com.example.club.shared.user.profile.domain.usecase

import com.example.club.shared.user.profile.domain.entity.User
import com.example.club.shared.user.profile.domain.repository.ProfileRepository

class GetProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(login:String,token:String): User =
        profileRepository.get(login,token)
}