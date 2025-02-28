package com.example.club.authorization.domain.usecase

import com.example.club.authorization.domain.entity.RefreshTokenResponse
import com.example.club.authorization.domain.repository.RefreshTokenRepository

class RefreshTokenUseCase(private val refreshTokenRepository: RefreshTokenRepository) {
    suspend operator fun invoke(refreshToken: String): RefreshTokenResponse =
        refreshTokenRepository.refreshToken(refreshToken)
}