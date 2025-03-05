package com.example.club.purchase.domain.repository

import com.example.club.purchase.domain.entity.PurchaseRequest
import com.example.club.purchase.domain.entity.PurchaseResponse

interface PurchaseRepository {
    suspend fun buyTicket(purchaseRequest: PurchaseRequest,token:String):PurchaseResponse
}