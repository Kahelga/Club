package com.example.club.shared.report.data.network

import com.example.club.shared.report.data.model.ReportUserModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UserReportApi {
    @GET("admin/report/users")
    suspend fun getUserReports(
        @Header("Authorization") token: String,
        @Header("startDate") startDate: String,
        @Header("endDate") endDate: String
    ): List<ReportUserModel>
}