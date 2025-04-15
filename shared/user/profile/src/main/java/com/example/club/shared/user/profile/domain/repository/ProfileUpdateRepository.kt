package com.example.club.shared.user.profile.domain.repository

import com.example.club.shared.user.profile.domain.entity.User

interface ProfileUpdateRepository {
    suspend fun update(login: String,token:String,newUser: User): User
}