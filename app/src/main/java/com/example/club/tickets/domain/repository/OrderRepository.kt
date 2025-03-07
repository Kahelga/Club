package com.example.club.tickets.domain.repository

import com.example.club.tickets.domain.entity.Order

interface OrderRepository {
    suspend fun getOrder(token:String):List<Order>
}