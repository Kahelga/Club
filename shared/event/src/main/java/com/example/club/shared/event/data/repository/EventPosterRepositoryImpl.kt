package com.example.club.shared.event.data.repository

import com.example.club.shared.event.data.converter.EventPosterConverter
import com.example.club.shared.event.data.network.EventPosterApi
import com.example.club.shared.event.domain.entity.Event
import com.example.club.shared.event.domain.repository.EventPosterRepository


class EventPosterRepositoryImpl(private val eventPosterApi: EventPosterApi, private val eventPosterConverter: EventPosterConverter):
    EventPosterRepository {
    override suspend fun getAll(): /*EventResponse*/List<Event> {
        val models = eventPosterApi.getAll()
        return models.map { eventPosterConverter.convert(it) }
    }

}