package com.example.club.shared.user.profile.data.repository

import com.example.club.shared.user.profile.data.converter.UserConverter
import com.example.club.shared.user.profile.data.network.ProfileApi
import com.example.club.shared.user.profile.domain.entity.User
import com.example.club.shared.user.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val userConverter: UserConverter
): ProfileRepository {
    override suspend fun get(login: String,token:String): User {
        val model=profileApi.get(login,token)
        return userConverter.convert(model)
    }
}