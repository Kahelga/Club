package com.example.club.shared.report.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReportEventModel(
    val eventId: String,
    val report: ReportModel
)
