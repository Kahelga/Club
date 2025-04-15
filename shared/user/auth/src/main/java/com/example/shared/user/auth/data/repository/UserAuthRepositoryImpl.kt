package com.example.shared.user.auth.data.repository

import com.example.shared.user.auth.data.converter.AuthConvert
import com.example.shared.user.auth.data.model.AuthRequestModel
import com.example.shared.user.auth.data.network.UserAuthApi
import com.example.shared.user.auth.domain.entity.AuthResponse
import com.example.shared.user.auth.domain.repository.UserAuthRepository

class UserAuthRepositoryImpl(
    private val userAuthApi: UserAuthApi,
    private val authConvert: AuthConvert
) : UserAuthRepository {

    override suspend fun signIn(login: String, pass: String): AuthResponse {
        val request = AuthRequestModel(login, pass)
        val response = userAuthApi.signIn(request)
        return authConvert.convert(response)
    }
}