package com.example.club.feature.purchase.presentation

import com.example.club.shared.tickets.domain.entity.Order
import com.example.club.shared.tickets.domain.entity.PurchaseResponse

interface PurchaseState {
    data object Initial : PurchaseState
    data object Loading : PurchaseState
    data class Failure(val message: String?) : PurchaseState
    data class Success(val response: Order/*PurchaseResponse*/) : PurchaseState
}