package com.example.club.shared.tickets.data.repository

import com.example.club.shared.tickets.data.converter.OrderConverter
import com.example.club.shared.tickets.data.converter.PurchaseConverter
import com.example.club.shared.tickets.data.model.PurchaseRequestModel
import com.example.club.shared.tickets.data.network.PurchaseApi
import com.example.club.shared.tickets.domain.entity.Order
import com.example.club.shared.tickets.domain.entity.PurchaseRequest
import com.example.club.shared.tickets.domain.entity.PurchaseResponse
import com.example.club.shared.tickets.domain.repository.PurchaseRepository

class PurchaseRepositoryImpl(
    private val purchaseApi: PurchaseApi,
    private val orderConverter: OrderConverter
   // private val purchaseConverter: PurchaseConverter
): PurchaseRepository {
    override suspend fun buyTicket(purchaseRequest: PurchaseRequest, token:String): Order/*PurchaseResponse */{
        val request= PurchaseRequestModel(purchaseRequest.bookingId,purchaseRequest.userId)
        val response =purchaseApi.buyTicket(request,token)
        return orderConverter.convert(response) /*purchaseConverter.convert(response)*/
    }
}