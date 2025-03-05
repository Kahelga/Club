package com.example.club.purchase.data.model

import com.example.club.purchase.domain.entity.PurchaseStatus
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseResponseData(
    val ticketId:String,
    val ticketCode:String,
    val status: PurchaseStatus
)
