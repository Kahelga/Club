package com.example.club.purchase.domain.usecase

import com.example.club.purchase.domain.entity.PurchaseRequest
import com.example.club.purchase.domain.entity.PurchaseResponse
import com.example.club.purchase.domain.repository.PurchaseRepository

class PurchaseUseCase(private val purchaseRepository: PurchaseRepository) {
    suspend operator fun invoke(purchaseRequest: PurchaseRequest, token:String):PurchaseResponse=
        purchaseRepository.buyTicket(purchaseRequest,token)
}