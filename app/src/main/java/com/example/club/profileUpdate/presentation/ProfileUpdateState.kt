package com.example.club.profileUpdate.presentation

import com.example.club.profile.domain.entity.User

interface ProfileUpdateState {
    data object Initial :ProfileUpdateState
    data object Loading :ProfileUpdateState
    data class Failure(val message: String?) :ProfileUpdateState
    data class Success(val user: User) :ProfileUpdateState
}