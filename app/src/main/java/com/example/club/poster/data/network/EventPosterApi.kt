package com.example.club.poster.data.network

import com.example.club.poster.data.model.EventModel
import retrofit2.http.GET

interface EventPosterApi {
    @GET("events/preview")
    suspend fun getAll(): List<EventModel>//EventResponseData
}