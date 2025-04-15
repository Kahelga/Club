package com.example.club.feature.admin.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.shared.report.domain.usecase.GetReportEventUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase

class ReportEventViewModelFactory (private val refreshTokenUseCase: RefreshTokenUseCase,
                                   private val tokenManager: TokenManager,
                                   private val getReportEventUseCase: GetReportEventUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ReportEventViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return ReportEventViewModel(refreshTokenUseCase,tokenManager,getReportEventUseCase) as T
    }
}