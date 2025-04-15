package com.example.club.shared.tickets.data.repository

import com.example.club.shared.tickets.data.converter.OrderConverter
import com.example.club.shared.tickets.data.network.OrderApi
import com.example.club.shared.tickets.domain.entity.Order
import com.example.club.shared.tickets.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val orderApi: OrderApi,
    private val orderConverter: OrderConverter
): OrderRepository {
  override suspend fun getOrder(token:String): List<Order>{
      val model=  orderApi.getOrder(token)
      return model.map { orderConverter.convert(it) }
    }
}