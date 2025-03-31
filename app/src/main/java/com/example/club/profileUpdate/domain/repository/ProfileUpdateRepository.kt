package com.example.club.profileUpdate.domain.repository

import com.example.club.profile.domain.entity.User

interface ProfileUpdateRepository {
    suspend fun update(login: String,token:String,newUser: User):User
}