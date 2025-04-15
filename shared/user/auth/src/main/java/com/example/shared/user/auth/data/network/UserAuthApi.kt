package com.example.shared.user.auth.data.network

import com.example.shared.user.auth.data.model.AuthResponseData
import com.example.shared.user.auth.data.model.AuthRequestModel
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAuthApi {
    @POST("api/v1/auth/login")
    suspend  fun signIn(@Body request: AuthRequestModel): AuthResponseData
}