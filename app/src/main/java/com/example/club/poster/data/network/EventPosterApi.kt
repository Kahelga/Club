package com.example.club.poster.data.network

import com.example.club.poster.data.EventResponseData
import retrofit2.http.GET

interface EventPosterApi {
    @GET("events")
    suspend fun getAll(): EventResponseData
}