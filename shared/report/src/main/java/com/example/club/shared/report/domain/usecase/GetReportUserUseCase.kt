package com.example.club.shared.report.domain.usecase

import com.example.club.shared.report.domain.entity.ReportUser
import com.example.club.shared.report.domain.repository.UserReportRepository


class GetReportUserUseCase (private val repository: UserReportRepository) {
    suspend operator fun invoke(token:String,startDate: String,endDate: String): List<ReportUser> = repository.getUserReports(token,startDate,endDate)

}