package com.example.club.purchase.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequestModel(
    val eventId:String,
    val seats:List<String>
)
