package com.example.club.shared.report.data.converter

import com.example.club.shared.report.data.model.ReportEventModel
import com.example.club.shared.report.data.model.ReportModel
import com.example.club.shared.report.data.model.ReportPeriodModel
import com.example.club.shared.report.data.model.ReportUserModel
import com.example.club.shared.report.domain.entity.Report
import com.example.club.shared.report.domain.entity.ReportEvent
import com.example.club.shared.report.domain.entity.ReportPeriod
import com.example.club.shared.report.domain.entity.ReportUser

class ReportConverter {
   private fun convertReport(model: ReportModel): Report =
        Report(
            profit =model.profit,
            ticketSold = model.ticketSold,
            expenses = model.expenses,
            netProfit = model.netProfit
        )
    fun convertEventReport(model: ReportEventModel): ReportEvent =
        ReportEvent(
            eventId = model.eventId,
            report = convertReport(model.report)
        )

    fun convertUserReport(model: ReportUserModel): ReportUser =
        ReportUser(
            userId = model.userId,
            report = convertReport(model.report)
        )

    fun convertPeriodReport(model: ReportPeriodModel): ReportPeriod =
        ReportPeriod(
            startDate = model.startDate,
            endDate = model.endDate,
            report = convertReport(model.report)
        )
}