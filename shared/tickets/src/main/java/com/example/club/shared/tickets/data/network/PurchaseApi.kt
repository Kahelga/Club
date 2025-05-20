package com.example.club.shared.tickets.data.network

import com.example.club.shared.tickets.data.model.OrderModel
import com.example.club.shared.tickets.data.model.PurchaseRequestModel
import com.example.club.shared.tickets.data.model.PurchaseResponseData
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PurchaseApi {
    @POST("tickets")
    suspend fun buyTicket(
        @Body request: PurchaseRequestModel,
        @Header("Authorization") token: String
    ): OrderModel /*PurchaseResponseData*/

}