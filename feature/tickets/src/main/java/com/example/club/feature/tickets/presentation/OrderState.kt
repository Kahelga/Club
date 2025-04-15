package com.example.club.feature.tickets.presentation

import com.example.club.shared.tickets.domain.entity.Order

interface OrderState {
    data object Initial : OrderState
    data object Loading : OrderState
    data class Failure(val message: String?) : OrderState
    data class Content(val orders:List<Order>): OrderState
}