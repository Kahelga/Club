package com.example.club.registration.data.network

import com.example.club.registration.data.model.RegRequestModel
import com.example.club.registration.data.model.RegResponseData
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRegApi {
    @POST("api/v1/auth/register")
    suspend  fun register(@Body request: RegRequestModel):RegResponseData
}