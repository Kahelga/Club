package com.example.club.feature.admin.reports.presentation

import com.example.club.shared.report.domain.entity.ReportUser

interface ReportUserState {
    data object Initial : ReportUserState
    data object Loading : ReportUserState
    data class Failure(val message: String?) : ReportUserState
    data class Content(val reportUser: List<ReportUser>) : ReportUserState
}