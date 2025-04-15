package com.example.club.shared.tickets.domain.repository

import com.example.club.shared.tickets.domain.entity.Order

interface OrderRepository {
    suspend fun getOrder(token:String):List<Order>
}