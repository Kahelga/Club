package com.example.club.shared.report.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportPeriodModel(
    val startDate: String,
    val endDate: String,
    val report: ReportModel
)
