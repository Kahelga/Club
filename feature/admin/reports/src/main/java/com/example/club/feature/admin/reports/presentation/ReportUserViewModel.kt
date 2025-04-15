package com.example.club.feature.admin.reports.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.club.shared.report.domain.usecase.GetReportUserUseCase
import com.example.club.util.manager.token.TokenManager
import com.example.shared.user.auth.domain.usecase.RefreshTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportUserViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val tokenManager: TokenManager,
    private val getReportUserUseCase: GetReportUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow<ReportUserState>(ReportUserState.Initial)
    val state: StateFlow<ReportUserState> = _state
    private var accessToken: String? = null
    fun loadReportUser( startDate:String,endDate:String){
        viewModelScope.launch {
            _state.value = ReportUserState.Loading
            try {
                accessToken = tokenManager.getAccessToken()
                val reportUser = getReportUserUseCase("Bearer $accessToken",startDate,endDate)
                _state.value = ReportUserState.Content(reportUser)
            } catch (ex: Exception) {
                Log.e("Error", "Exception occurred", ex)
                if (ex.message?.contains("HTTP 401 Client Error", ignoreCase = true) == true) {

                    try {
                        val refreshToken = tokenManager.getRefreshToken()
                        if (refreshToken != null) {
                            val newToken = refreshTokenUseCase(refreshToken)
                            accessToken = newToken.accessToken
                            tokenManager.updateAccessToken(accessToken)
                            val reportUser = getReportUserUseCase("Bearer $accessToken",startDate,endDate)
                            _state.value = ReportUserState.Content(reportUser)
                        } else {
                            _state.value =  ReportUserState.Failure("Refresh token is not available.")
                        }
                    } catch (refreshEx: Exception) {
                        _state.value =  ReportUserState.Failure(refreshEx.message)
                    }
                } else {
                    _state.value =  ReportUserState.Failure(ex.message)
                }
            }
        }
    }
}