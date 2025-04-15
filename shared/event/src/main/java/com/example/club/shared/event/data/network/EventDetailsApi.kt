package com.example.club.shared.event.data.network

import com.example.club.shared.event.data.model.EventDetailsModel
import retrofit2.http.GET
import retrofit2.http.Path

interface EventDetailsApi {
    @GET("events/{eventId}")
    suspend fun get(@Path("eventId") eventId: String): EventDetailsModel //EventDetailsResponseData
}