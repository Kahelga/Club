package com.example.club.profileUpdate.data.network

import com.example.club.profile.data.model.UserModel
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileUpdateApi {
    @PUT("users/{login}/profile")
    suspend fun updateProfile(
        @Path("login") login: String,
        @Header("Authorization") token: String,
        @Body userProfile: UserModel
    ): UserModel
}