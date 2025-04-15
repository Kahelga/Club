package com.example.club.shared.report.data.repository

import com.example.club.shared.report.data.converter.ReportConverter
import com.example.club.shared.report.data.network.EventReportApi
import com.example.club.shared.report.domain.entity.ReportEvent
import com.example.club.shared.report.domain.repository.EventReportRepository


class EventReportRepositoryImpl(
    private val eventReportApi: EventReportApi,
    private val reportConverter: ReportConverter
):EventReportRepository {
    override suspend fun getEventReports(
        token: String,
        startDate: String,
        endDate: String
    ): List<ReportEvent> {
        val eventReport = eventReportApi.getEventReports(token,startDate,endDate)
        return eventReport.map { reportConverter.convertEventReport(it)}
    }
}