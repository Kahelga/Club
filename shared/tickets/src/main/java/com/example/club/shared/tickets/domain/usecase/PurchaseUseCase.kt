package com.example.club.shared.tickets.domain.usecase

import com.example.club.shared.tickets.domain.entity.PurchaseRequest
import com.example.club.shared.tickets.domain.entity.PurchaseResponse
import com.example.club.shared.tickets.domain.repository.PurchaseRepository

class PurchaseUseCase(private val purchaseRepository: PurchaseRepository) {
    suspend operator fun invoke(purchaseRequest: PurchaseRequest, token:String): PurchaseResponse =
        purchaseRepository.buyTicket(purchaseRequest,token)
}