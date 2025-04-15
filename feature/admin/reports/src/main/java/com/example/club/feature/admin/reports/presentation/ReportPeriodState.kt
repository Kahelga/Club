package com.example.club.feature.admin.reports.presentation

import com.example.club.shared.report.domain.entity.ReportPeriod

interface ReportPeriodState {
    data object Initial : ReportPeriodState
    data object Loading : ReportPeriodState
    data class Failure(val message: String?) : ReportPeriodState
    data class Content(val reportPeriod: ReportPeriod) : ReportPeriodState
}