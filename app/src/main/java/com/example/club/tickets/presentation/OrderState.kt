package com.example.club.tickets.presentation

import com.example.club.tickets.domain.entity.Order

interface OrderState {
    data object Initial :OrderState
    data object Loading :OrderState
    data class Failure(val message: String?) :OrderState
    data class Content(val orders:List<Order>):OrderState
}