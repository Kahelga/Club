package com.example.club.profile.data.network

import com.example.club.profile.data.model.UserModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProfileApi {
    @GET("users/{login}/profile")
    suspend fun get(
        @Path("login") login: String,
        @Header("Authorization") token: String
    ): UserModel

}