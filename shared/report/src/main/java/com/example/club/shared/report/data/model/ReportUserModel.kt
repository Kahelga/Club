package com.example.club.shared.report.data.model


import kotlinx.serialization.Serializable

@Serializable
data class ReportUserModel(
    val userId: String,
    val report: ReportModel
)
