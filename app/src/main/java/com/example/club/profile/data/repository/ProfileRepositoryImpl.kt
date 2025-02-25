package com.example.club.profile.data.repository

import com.example.club.profile.data.converter.UserConverter
import com.example.club.profile.data.network.ProfileApi
import com.example.club.profile.domain.entity.User
import com.example.club.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val userConverter: UserConverter
):ProfileRepository {
    override suspend fun get(userId: String): User {
        val model=profileApi.get(userId)
        return userConverter.convert(model)
    }
}