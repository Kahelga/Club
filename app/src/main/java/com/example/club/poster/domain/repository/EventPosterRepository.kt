package com.example.club.poster.domain.repository

import com.example.club.poster.domain.EventResponse

interface EventPosterRepository {
    suspend fun getAll():EventResponse
}