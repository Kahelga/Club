package com.example.club.shared.tickets.data.network

import com.example.club.shared.tickets.data.model.CancelOrderRequestModel
import com.example.club.shared.tickets.data.model.OrderModel
import retrofit2.http.Body

import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface CancelOrderApi {
    //@PUT("bookings/{id}/confirm")
    @PUT("bookings/confirm")
    suspend fun cancelOrder(
       // @Path("bookedId") eventId: String,
        @Body request:CancelOrderRequestModel,
        @Header("Authorization") token: String):OrderModel
}