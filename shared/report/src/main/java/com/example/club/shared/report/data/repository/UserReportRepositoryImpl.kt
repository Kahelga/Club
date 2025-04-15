package com.example.club.shared.report.data.repository

import com.example.club.shared.report.data.converter.ReportConverter
import com.example.club.shared.report.data.network.UserReportApi
import com.example.club.shared.report.domain.entity.ReportUser
import com.example.club.shared.report.domain.repository.UserReportRepository

class UserReportRepositoryImpl(
    private val userReportApi: UserReportApi,
    private val reportConverter: ReportConverter

):UserReportRepository {
    override suspend fun getUserReports(
        token: String,
        startDate: String,
        endDate: String
    ): List<ReportUser> {
        val userReport = userReportApi.getUserReports(token,startDate,endDate)
        return userReport.map { reportConverter.convertUserReport(it)}
    }

}