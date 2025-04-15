package com.example.club.shared.report.domain.repository

import com.example.club.shared.report.domain.entity.ReportUser

interface UserReportRepository {
    suspend fun getUserReports(token:String,startDate: String,endDate: String): List<ReportUser>
}