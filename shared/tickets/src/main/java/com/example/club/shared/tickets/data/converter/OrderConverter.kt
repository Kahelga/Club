package com.example.club.shared.tickets.data.converter

import com.example.club.shared.event.data.converter.EventPosterConverter
import com.example.club.shared.tickets.data.model.OrderModel
import com.example.club.shared.tickets.domain.entity.Order

class OrderConverter {
    val eventPosterConverter= EventPosterConverter()
    fun convert(orderModel: OrderModel): Order {
        return Order(
            ticketId = orderModel.ticketId,
            userId = orderModel.userId,
            event =eventPosterConverter.convert(orderModel.event),
            seats = orderModel.seats,
            issueDate = orderModel.issueDate,
            status = orderModel.status
        )
    }
}