package com.example.club.shared.tickets.data.repository

import com.example.club.shared.tickets.data.converter.OrderConverter
import com.example.club.shared.tickets.data.model.CancelOrderRequestModel
import com.example.club.shared.tickets.data.network.CancelOrderApi
import com.example.club.shared.tickets.domain.entity.CancelOrderRequest
import com.example.club.shared.tickets.domain.entity.Order
import com.example.club.shared.tickets.domain.repository.CancelOrderRepository

class CancelOrderRepositoryImpl(
    private val cancelOrderApi: CancelOrderApi,
    private val orderConverter: OrderConverter
): CancelOrderRepository {
    override suspend fun cancelOrder(/*bookingId:String*/cancelOrderRequest: CancelOrderRequest, token: String): Order {
       val  request= CancelOrderRequestModel(cancelOrderRequest.bookingId)
        val  response = cancelOrderApi.cancelOrder(/*bookingId*/request,token)
        return orderConverter.convert(response)
    }
}