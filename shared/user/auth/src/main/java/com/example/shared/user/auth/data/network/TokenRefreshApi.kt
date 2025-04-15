package com.example.shared.user.auth.data.network

import com.example.shared.user.auth.data.model.RefreshTokenRequestModel
import com.example.shared.user.auth.data.model.RefreshTokenResponseData
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenRefreshApi {
    @POST("auth/login/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequestModel): RefreshTokenResponseData
}
