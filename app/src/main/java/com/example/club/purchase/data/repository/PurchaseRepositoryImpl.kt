package com.example.club.purchase.data.repository

import com.example.club.authorization.data.model.AuthRequestModel
import com.example.club.purchase.data.converter.PurchaseConverter
import com.example.club.purchase.data.model.PurchaseRequestModel
import com.example.club.purchase.data.network.PurchaseApi
import com.example.club.purchase.domain.entity.PurchaseRequest
import com.example.club.purchase.domain.entity.PurchaseResponse
import com.example.club.purchase.domain.repository.PurchaseRepository

class PurchaseRepositoryImpl(
    private val purchaseApi: PurchaseApi,
    private val purchaseConverter: PurchaseConverter
):PurchaseRepository {
    override suspend fun buyTicket(purchaseRequest: PurchaseRequest, token:String):PurchaseResponse{
        val request= PurchaseRequestModel(purchaseRequest.eventId,purchaseRequest.seats)
        val response =purchaseApi.buyTicket(request,token)
        return purchaseConverter.convert(response)
    }
}