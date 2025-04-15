package com.example.club.shared.user.profile.domain.repository

import com.example.club.shared.user.profile.domain.entity.User

interface ProfileRepository {
    suspend fun get(login: String,token:String): User
}