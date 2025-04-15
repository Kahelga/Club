package com.example.club.shared.report.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportModel(
    val profit: Double,
    val ticketSold: Int,
    val expenses: Double,
    val netProfit: Double
)