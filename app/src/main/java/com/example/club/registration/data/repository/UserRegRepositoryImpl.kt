package com.example.club.registration.data.repository

import com.example.club.registration.data.converter.RegConvert
import com.example.club.registration.data.model.RegRequestModel
import com.example.club.registration.data.network.UserRegApi
import com.example.club.registration.domain.entity.RegResponse
import com.example.club.registration.domain.repository.UserRegRepository

class UserRegRepositoryImpl(
    private val userRegApi: UserRegApi,
    private val regConvert: RegConvert
):UserRegRepository {
    override suspend fun register(name: String, email: String, pass: String): RegResponse {
        val request=RegRequestModel(name,email,pass)
        val response=userRegApi.register(request)
        return regConvert.convert(response)
    }

}