package com.example.club.authorization.domain.repository

import com.example.club.authorization.domain.entity.RefreshTokenResponse

interface RefreshTokenRepository {
    suspend fun refreshToken(accessToken: String):RefreshTokenResponse
}