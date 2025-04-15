package com.example.club.shared.report.data.network

import com.example.club.shared.report.data.model.ReportPeriodModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ReportPeriodApi {
    @GET("admin/reports")
    suspend fun getReports(
        @Header("Authorization") token: String,
        @Header("startDate") startDate: String,
        @Header("endDate") endDate: String
    ): ReportPeriodModel
}