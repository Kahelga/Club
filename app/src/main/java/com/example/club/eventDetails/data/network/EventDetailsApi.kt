package com.example.club.eventDetails.data.network

import com.example.club.eventDetails.data.EventDetailsResponseData
import com.example.club.eventDetails.data.model.EventDetailsModel
import retrofit2.http.GET
import retrofit2.http.Path

interface EventDetailsApi {
    @GET("events/{eventId}")
    suspend fun get(@Path("eventId") eventId: String):EventDetailsModel //EventDetailsResponseData
}