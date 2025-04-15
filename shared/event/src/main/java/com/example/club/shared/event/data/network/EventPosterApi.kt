package com.example.club.shared.event.data.network

import com.example.club.shared.event.data.model.EventModel
import retrofit2.http.GET

interface EventPosterApi {
    @GET("events/preview")
    suspend fun getAll(): List<EventModel>//EventResponseData
}