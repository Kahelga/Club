package com.example.club.shared.report.domain.usecase

import com.example.club.shared.report.domain.entity.ReportEvent
import com.example.club.shared.report.domain.entity.ReportPeriod
import com.example.club.shared.report.domain.repository.EventReportRepository


class GetReportEventUseCase(private val repository: EventReportRepository) {
    suspend operator fun invoke(token:String,startDate: String,endDate: String): List<ReportEvent> = repository.getEventReports(token,startDate,endDate)

}