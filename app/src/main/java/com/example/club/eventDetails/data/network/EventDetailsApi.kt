package com.example.club.eventDetails.data.network

import com.example.club.eventDetails.data.EventDetailsResponseData
import retrofit2.http.GET
import retrofit2.http.Path

interface EventDetailsApi {
    @GET("event/{eventId}")
    suspend fun get(@Path("eventId") eventId: String): EventDetailsResponseData
}