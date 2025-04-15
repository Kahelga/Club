package com.example.club.feature.admin.reports.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.shared.report.domain.usecase.GetReportPeriodUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportPeriodViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val getReportPeriodUseCase: GetReportPeriodUseCase
) : ViewModel(){

    private val _state = MutableStateFlow<ReportPeriodState>(ReportPeriodState.Initial)
    val state: StateFlow<ReportPeriodState> = _state
    private var accessToken: String? = null
    fun loadReportPeriod( startDate:String,endDate:String){
        viewModelScope.launch {
            _state.value = ReportPeriodState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val reportPeriod = getReportPeriodUseCase("Bearer $accessToken",startDate,endDate)
                _state.value = ReportPeriodState.Content(reportPeriod)
            } catch (ex: Exception) {
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)
                            val reportPeriod = getReportPeriodUseCase("Bearer $accessToken",startDate,endDate)
                            _state.value = ReportPeriodState.Content(reportPeriod)
                        } else {
                            _state.value = ReportPeriodState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value = ReportPeriodState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value = ReportPeriodState.Failure(ex.message)
                }
            }
        }
    }
}