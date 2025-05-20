package com.example.club.shared.tickets.data.network

import com.example.club.shared.tickets.data.model.BookingRequestModel
import com.example.club.shared.tickets.data.model.BookingResponseData
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BookingApi {
    @POST("bookings")
    suspend fun bookedTicket(
        @Body request: BookingRequestModel,
        @Header("Authorization") token: String
    ): BookingResponseData
}