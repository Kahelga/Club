package com.example.club.authorization.data.network

import com.example.club.authorization.data.model.AuthResponseData
import com.example.club.authorization.data.model.RefreshTokenRequestModel
import com.example.club.authorization.data.model.RefreshTokenResponseData
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRefreshApi {
    @POST("auth/login/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequestModel): RefreshTokenResponseData
}
