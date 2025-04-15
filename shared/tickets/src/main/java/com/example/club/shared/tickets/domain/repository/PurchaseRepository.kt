package com.example.club.shared.tickets.domain.repository

import com.example.club.shared.tickets.domain.entity.PurchaseRequest
import com.example.club.shared.tickets.domain.entity.PurchaseResponse

interface PurchaseRepository {
    suspend fun buyTicket(purchaseRequest: PurchaseRequest, token:String): PurchaseResponse
}