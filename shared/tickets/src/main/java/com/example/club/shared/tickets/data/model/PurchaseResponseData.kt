package com.example.club.shared.tickets.data.model

import com.example.club.shared.tickets.domain.entity.PurchaseStatus
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseResponseData(
    val ticketId:String,
    val ticketCode:String,
    val status: PurchaseStatus
)
