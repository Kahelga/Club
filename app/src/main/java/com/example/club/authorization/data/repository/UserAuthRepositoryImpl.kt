package com.example.club.authorization.data.repository

import com.example.club.authorization.data.converter.UserConvert
import com.example.club.authorization.data.model.AuthRequestModel
import com.example.club.authorization.data.network.UserAuthApi
import com.example.club.authorization.domain.AuthResponse
import com.example.club.authorization.domain.repository.UserAuthRepository

class UserAuthRepositoryImpl(
    private val userAuthApi: UserAuthApi,
    private val userConvert: UserConvert
) : UserAuthRepository {

    override suspend fun signIn(login: String, pass: String): AuthResponse {
        val request = AuthRequestModel(login, pass)
        val response = userAuthApi.signIn(request)

        return userConvert.convert(response)
    }
}