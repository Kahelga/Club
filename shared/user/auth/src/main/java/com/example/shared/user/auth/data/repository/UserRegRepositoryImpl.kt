package com.example.shared.user.auth.data.repository

import com.example.shared.user.auth.data.converter.RegConvert
import com.example.shared.user.auth.data.model.RegRequestModel
import com.example.shared.user.auth.data.network.UserRegApi
import com.example.shared.user.auth.domain.entity.RegResponse
import com.example.shared.user.auth.domain.repository.UserRegRepository

class UserRegRepositoryImpl(
    private val userRegApi: UserRegApi,
    private val regConvert: RegConvert
): UserRegRepository {
    override suspend fun register(name: String, email: String, pass: String): RegResponse {
        val request= RegRequestModel(name,email,pass)
        val response=userRegApi.register(request)
        return regConvert.convert(response)
    }

}