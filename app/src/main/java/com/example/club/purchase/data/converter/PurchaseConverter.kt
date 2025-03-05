package com.example.club.purchase.data.converter

import com.example.club.purchase.data.model.PurchaseResponseData
import com.example.club.purchase.domain.entity.PurchaseResponse

class PurchaseConverter {
    fun convert(purchaseResponseData: PurchaseResponseData):PurchaseResponse{
        return PurchaseResponse(
            ticketId = purchaseResponseData.ticketId,
            ticketCode = purchaseResponseData.ticketCode,
            status = purchaseResponseData.status
        )
    }
}