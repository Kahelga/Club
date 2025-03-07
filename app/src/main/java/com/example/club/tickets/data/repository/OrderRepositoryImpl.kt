package com.example.club.tickets.data.repository

import com.example.club.tickets.data.converter.OrderConverter
import com.example.club.tickets.data.network.OrderApi
import com.example.club.tickets.domain.entity.Order
import com.example.club.tickets.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val orderApi: OrderApi,
    private val orderConverter: OrderConverter
):OrderRepository {
  override suspend fun getOrder(token:String): List<Order>{
      val model=  orderApi.getOrder(token)
      return model.map { orderConverter.convert(it) }
    }
}