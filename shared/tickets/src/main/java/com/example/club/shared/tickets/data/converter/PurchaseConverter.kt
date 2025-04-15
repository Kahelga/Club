package com.example.club.shared.tickets.data.converter

import com.example.club.shared.tickets.data.model.PurchaseResponseData
import com.example.club.shared.tickets.domain.entity.PurchaseResponse

class PurchaseConverter {
    fun convert(purchaseResponseData: PurchaseResponseData): PurchaseResponse {
        return PurchaseResponse(
            ticketId = purchaseResponseData.ticketId,
            ticketCode = purchaseResponseData.ticketCode,
            status = purchaseResponseData.status
        )
    }
}