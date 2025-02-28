package com.example.club.authorization.data.network

import com.example.club.authorization.data.model.AuthResponseData
import com.example.club.authorization.data.model.AuthRequestModel
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAuthApi {
    @POST("users/login")
    suspend  fun signIn(@Body request: AuthRequestModel): AuthResponseData
}