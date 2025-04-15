package com.example.club.shared.event.data.network

import com.example.club.shared.event.data.model.EventDetailsModel
import retrofit2.http.GET
import retrofit2.http.Header

interface EventsAdminApi {
    @GET("admin/events")
    suspend fun getAll(@Header("Authorization") token: String): List<EventDetailsModel>
}