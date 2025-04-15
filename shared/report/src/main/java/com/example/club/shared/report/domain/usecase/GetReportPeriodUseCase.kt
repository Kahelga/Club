package com.example.club.shared.report.domain.usecase

import com.example.club.shared.report.domain.entity.ReportPeriod
import com.example.club.shared.report.domain.repository.PeriodReportRepository

class GetReportPeriodUseCase(private val repository:PeriodReportRepository) {
    suspend operator fun invoke(token:String,startDate: String,endDate: String): ReportPeriod = repository.getReports(token,startDate,endDate)

}