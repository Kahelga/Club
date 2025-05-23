package com.example.club.shared.tickets.data.network

import com.example.club.shared.tickets.data.model.OrderModel
import retrofit2.http.GET
import retrofit2.http.Header

interface OrderApi {
    @GET("tickets")
    suspend fun getOrder(@Header("Authorization") token: String):List<OrderModel>
}