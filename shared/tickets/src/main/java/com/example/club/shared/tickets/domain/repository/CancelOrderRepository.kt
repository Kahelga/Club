package com.example.club.shared.tickets.domain.repository

import com.example.club.shared.tickets.domain.entity.CancelOrderRequest
import com.example.club.shared.tickets.domain.entity.Order

interface CancelOrderRepository {
    suspend fun cancelOrder(/*bookingId: String*/cancelOrderRequest: CancelOrderRequest,token:String): Order
}