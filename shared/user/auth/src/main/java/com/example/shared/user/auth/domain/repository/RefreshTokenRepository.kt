package com.example.shared.user.auth.domain.repository

import com.example.shared.user.auth.domain.entity.RefreshTokenResponse

interface RefreshTokenRepository {
    suspend fun refreshToken(accessToken: String): RefreshTokenResponse
}