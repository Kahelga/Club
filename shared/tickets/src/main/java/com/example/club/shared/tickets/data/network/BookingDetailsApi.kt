package com.example.club.shared.tickets.data.network

import com.example.club.shared.tickets.data.model.BookedTicketModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BookingDetailsApi {
    @GET("/bookings/{bookingId}")
    suspend fun bookedTicket(
        @Path("bookingId") bookingId:String,
        @Header("Authorization") token: String
    ): BookedTicketModel
}