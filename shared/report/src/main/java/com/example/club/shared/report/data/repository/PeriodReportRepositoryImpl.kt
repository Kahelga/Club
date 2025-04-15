package com.example.club.shared.report.data.repository

import com.example.club.shared.report.data.converter.ReportConverter
import com.example.club.shared.report.data.network.ReportPeriodApi
import com.example.club.shared.report.domain.entity.ReportPeriod
import com.example.club.shared.report.domain.repository.PeriodReportRepository

class PeriodReportRepositoryImpl(
    private val periodReportApi: ReportPeriodApi,
    private val reportConverter: ReportConverter
):PeriodReportRepository {
    override suspend fun getReports(
        token: String,
        startDate: String,
        endDate: String
    ): ReportPeriod {
        val periodReport = periodReportApi.getReports(token,startDate,endDate)
        return  reportConverter.convertPeriodReport(periodReport)
    }


}