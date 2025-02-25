package com.example.club.profile.data.network

import com.example.club.profile.data.ProfileResponseData
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {
    @GET("user/{userId}") //login
    suspend fun get(@Path("userId") userId: String):ProfileResponseData
}