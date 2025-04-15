package com.example.club.feature.admin.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.shared.report.domain.usecase.GetReportPeriodUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase

class ReportPeriodViewModelFactory (private val refreshTokenUseCase: RefreshTokenUseCase,
                                    private val tokenManager: TokenManager,
                                    private val getReportPeriodUseCase: GetReportPeriodUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ReportPeriodViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return ReportPeriodViewModel(refreshTokenUseCase,tokenManager,getReportPeriodUseCase) as T
    }
}