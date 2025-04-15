package com.example.club.shared.user.profile.data.network

import com.example.club.shared.user.profile.data.model.UserModel
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