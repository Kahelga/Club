package com.example.club.purchase.data.network

import com.example.club.purchase.data.model.PurchaseRequestModel
import com.example.club.purchase.data.model.PurchaseResponseData
import com.example.club.purchase.domain.entity.PurchaseResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PurchaseApi {
    @POST("tickets")
    suspend fun buyTicket(
        @Body request: PurchaseRequestModel,
        @Header("Authorization") token: String
    ):PurchaseResponseData

}