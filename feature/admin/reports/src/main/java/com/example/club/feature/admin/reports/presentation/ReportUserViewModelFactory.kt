package com.example.club.feature.admin.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.club.shared.report.domain.usecase.GetReportUserUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase

class ReportUserViewModelFactory(private val refreshTokenUseCase: RefreshTokenUseCase,
                                 private val tokenManager: TokenManager,
                                 private val getReportUserUseCase: GetReportUserUseCase
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ReportUserViewModel::class.java) { "Unknown ViewModel: $modelClass" }
        return ReportUserViewModel(refreshTokenUseCase,tokenManager,getReportUserUseCase) as T
    }
}