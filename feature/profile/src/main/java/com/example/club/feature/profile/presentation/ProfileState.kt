package com.example.club.feature.profile.presentation

import com.example.club.shared.user.profile.domain.entity.User

interface ProfileState {
    data object Initial : ProfileState
    data object Loading : ProfileState
    data class Failure(val message: String?) : ProfileState
    data class Content(val user: User) : ProfileState
}