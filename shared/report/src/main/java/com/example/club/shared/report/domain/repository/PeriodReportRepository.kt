package com.example.club.shared.report.domain.repository

import com.example.club.shared.report.domain.entity.ReportPeriod

interface PeriodReportRepository {
    suspend fun getReports(token:String,startDate: String,endDate: String): ReportPeriod
}