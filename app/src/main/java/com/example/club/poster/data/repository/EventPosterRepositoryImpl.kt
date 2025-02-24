package com.example.club.poster.data.repository

import com.example.club.poster.data.converter.EventPosterConverter
import com.example.club.poster.data.network.EventPosterApi
import com.example.club.poster.domain.EventResponse
import com.example.club.poster.domain.repository.EventPosterRepository

class EventPosterRepositoryImpl(private val eventPosterApi: EventPosterApi, private val eventPosterConverter: EventPosterConverter):
    EventPosterRepository {
    override suspend fun getAll(): EventResponse {
        val models = eventPosterApi.getAll()
        return eventPosterConverter.convert(models)
    }

}