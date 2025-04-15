package com.example.club.shared.event.domain.repository


import com.example.club.shared.event.domain.entity.Hall

interface HallRepository {
    suspend fun get(eventId: String,token:String): Hall
}