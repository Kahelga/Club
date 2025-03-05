package com.example.club.hall.data.network

import com.example.club.hall.data.model.HallModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HallApi {
    @GET("events/{eventId}/hall")
    suspend fun get(
        @Path("eventId") eventId: String,
        @Header("Authorization") token: String
    ): HallModel
}