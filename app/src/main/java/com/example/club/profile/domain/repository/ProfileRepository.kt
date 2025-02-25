package com.example.club.profile.domain.repository

import com.example.club.profile.domain.entity.User

interface ProfileRepository {
    suspend fun get(login: String): User
}