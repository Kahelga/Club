package com.example.shared.user.auth.domain.usecase

import com.example.shared.user.auth.domain.entity.RefreshTokenResponse
import com.example.shared.user.auth.domain.repository.RefreshTokenRepository

class RefreshTokenUseCase(private val refreshTokenRepository: RefreshTokenRepository) {
    suspend operator fun invoke(refreshToken: String): RefreshTokenResponse =
        refreshTokenRepository.refreshToken(refreshToken)
}