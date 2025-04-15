package com.example.club.shared.report.data.network

import com.example.club.shared.report.data.model.ReportEventModel
import retrofit2.http.GET
import retrofit2.http.Header


interface EventReportApi {
    @GET("admin/report/events")
    suspend fun getEventReports(
        @Header("Authorization") token: String,
        @Header("startDate") startDate: String,
        @Header("endDate") endDate: String
    ): List<ReportEventModel>
}