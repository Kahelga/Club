package com.example.club.shared.report.domain.entity

data class Report(
    val profit: Double,    // Прибыль
    val ticketSold: Int,   // Количество проданных билетов
    val expenses: Double,  // Расходы
    val netProfit: Double  // Чистая прибыль
)
