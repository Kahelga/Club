package com.example.club.feature.admin.reports.presentation

import com.example.club.shared.report.domain.entity.ReportEvent


interface ReportEventState {
    data object Initial :  ReportEventState
    data object Loading :  ReportEventState
    data class Failure(val message: String?) :  ReportEventState
    data class Content(val reportEvent: List<ReportEvent>) :  ReportEventState
}