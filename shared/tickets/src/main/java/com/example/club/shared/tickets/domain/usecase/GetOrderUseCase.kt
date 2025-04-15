package com.example.club.shared.tickets.domain.usecase

import com.example.club.shared.tickets.domain.entity.Order
import com.example.club.shared.tickets.domain.repository.OrderRepository

class GetOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(token:String):List<Order> =
        orderRepository.getOrder(token)
}