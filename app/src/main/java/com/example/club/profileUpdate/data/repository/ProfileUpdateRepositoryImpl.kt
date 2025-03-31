package com.example.club.profileUpdate.data.repository

import com.example.club.profile.data.converter.UserConverter
import com.example.club.profile.data.model.UserModel
import com.example.club.profile.domain.entity.User
import com.example.club.profileUpdate.data.network.ProfileUpdateApi
import com.example.club.profileUpdate.domain.repository.ProfileUpdateRepository

class ProfileUpdateRepositoryImpl(
    private val profileUpdateApi: ProfileUpdateApi,
    private val userConverter: UserConverter
) : ProfileUpdateRepository {
    override suspend fun update(login: String, token: String, newUser: User): User {
        val request = UserModel(
            newUser.id,
            newUser.phone,
            newUser.lastname,
            newUser.firstname,
            newUser.middlename,
            newUser.email,
            newUser.city,
            newUser.role
        )
        val response=profileUpdateApi.updateProfile(login,token,request)
        return userConverter.convert(response)
    }

}