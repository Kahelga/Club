package com.example.club.authorization.data.repository

import com.example.club.authorization.data.converter.AuthConvert
import com.example.club.authorization.data.model.RefreshTokenRequestModel
import com.example.club.authorization.data.network.TokenRefreshApi
import com.example.club.authorization.domain.entity.RefreshTokenRequest
import com.example.club.authorization.domain.entity.RefreshTokenResponse
import com.example.club.authorization.domain.repository.RefreshTokenRepository

class RefreshTokenRepositoryImpl(
    private val refreshApi: TokenRefreshApi,
    private val authConvert: AuthConvert
): RefreshTokenRepository{
    override suspend fun refreshToken(refreshToken:String):RefreshTokenResponse{
        val request=RefreshTokenRequestModel(refreshToken)
        val response=refreshApi.refreshToken(request)
        return authConvert.convertToken(response)
    }
}