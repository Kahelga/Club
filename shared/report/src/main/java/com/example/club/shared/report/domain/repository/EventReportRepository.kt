package com.example.club.shared.report.domain.repository

import com.example.club.shared.report.domain.entity.ReportEvent

interface EventReportRepository {
    suspend fun getEventReports(token:String,startDate: String,endDate: String): List<ReportEvent>
}