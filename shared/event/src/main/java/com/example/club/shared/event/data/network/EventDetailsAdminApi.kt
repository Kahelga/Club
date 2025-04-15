package com.example.club.shared.event.data.network

import com.example.club.shared.event.data.model.EventDetailsModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface EventDetailsAdminApi {
    @GET("admin/events/{eventId}")
    suspend fun get(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): EventDetailsModel
}