package com.example.club.feature.tickets.presentation

import com.example.club.shared.tickets.domain.entity.Order

interface CancelOrderState {
    data object Initial:CancelOrderState
    data object Loading :CancelOrderState
    data class Failure(val message: String?) :CancelOrderState
    data class Success(val response: Order) :CancelOrderState
}