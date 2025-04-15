package com.example.shared.user.auth.data.repository

import com.example.shared.user.auth.data.converter.AuthConvert
import com.example.shared.user.auth.data.model.RefreshTokenRequestModel
import com.example.shared.user.auth.data.network.TokenRefreshApi
import com.example.shared.user.auth.domain.entity.RefreshTokenResponse
import com.example.shared.user.auth.domain.repository.RefreshTokenRepository

class RefreshTokenRepositoryImpl(
    private val refreshApi: TokenRefreshApi,
    private val authConvert: AuthConvert
): RefreshTokenRepository {
    override suspend fun refreshToken(refreshToken:String): RefreshTokenResponse {
        val request= RefreshTokenRequestModel(refreshToken)
        val response=refreshApi.refreshToken(request)
        return authConvert.convertToken(response)
    }
}