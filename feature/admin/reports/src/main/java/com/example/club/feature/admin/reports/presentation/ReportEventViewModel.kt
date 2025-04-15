package com.example.club.feature.admin.reports.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.shared.report.domain.usecase.GetReportEventUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportEventViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val getReportEventUseCase: GetReportEventUseCase
) : ViewModel() {
    private val _state = MutableStateFlow< ReportEventState>( ReportEventState.Initial)
    val state: StateFlow< ReportEventState> = _state
    private var accessToken: String? = null
    fun loadReportEvent( startDate:String,endDate:String){
        viewModelScope.launch {
            _state.value =  ReportEventState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val reportEvent = getReportEventUseCase("Bearer $accessToken",startDate,endDate)
                _state.value =  ReportEventState.Content(reportEvent)
            } catch (ex: Exception) {
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)
                            val reportEvent = getReportEventUseCase("Bearer $accessToken",startDate,endDate)
                            _state.value =  ReportEventState.Content(reportEvent)
                        } else {
                            _state.value =   ReportEventState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value =   ReportEventState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value =   ReportEventState.Failure(ex.message)
                }
            }
        }
    }
}