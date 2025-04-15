package com.example.club.shared.event.data.repository

import com.example.club.shared.event.data.converter.EventDetailsConverter
import com.example.club.shared.event.data.network.EventsAdminApi
import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.shared.event.domain.repository.EventsAdminRepository

class EventsAdminRepositoryImpl (private val eventsAdminApi: EventsAdminApi, private val eventDetailsConverter: EventDetailsConverter):
    EventsAdminRepository {
    override suspend fun getAll(token:String): /*EventResponse*/List<EventDetails> {
        val models = eventsAdminApi.getAll(token)
        return models.map { eventDetailsConverter.convert(it) }
    }

}