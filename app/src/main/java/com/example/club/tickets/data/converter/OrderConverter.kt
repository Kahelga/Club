package com.example.club.tickets.data.converter

import com.example.club.poster.data.converter.EventPosterConverter
import com.example.club.tickets.data.model.OrderModel
import com.example.club.tickets.domain.entity.Order

class OrderConverter {
    val eventPosterConverter=EventPosterConverter()
    fun convert(orderModel: OrderModel):Order{
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