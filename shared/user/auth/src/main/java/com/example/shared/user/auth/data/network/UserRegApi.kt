package com.example.shared.user.auth.data.network

import com.example.shared.user.auth.data.model.RegRequestModel
import com.example.shared.user.auth.data.model.RegResponseData
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRegApi {
    @POST("api/v1/auth/register")
    suspend  fun register(@Body request: RegRequestModel): RegResponseData
}