package com.example.club.hall.domain.repository

import com.example.club.hall.domain.entity.Hall

interface HallRepository {
    suspend fun get(eventId: String,token:String): Hall
}