package com.example.club.shared.event.data.repository

import com.example.club.shared.event.data.converter.EventDetailsConverter
import com.example.club.shared.event.data.network.EventDetailsApi
import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.shared.event.domain.repository.EventDetailsRepository

class EventRepositoryImpl(
    private val eventDetailsApi: EventDetailsApi,
    private val eventDetailsConverter: EventDetailsConverter
) : EventDetailsRepository {
    override suspend fun get(eventId: String): EventDetails {
        val model =  eventDetailsApi.get(eventId)
        return  eventDetailsConverter.convert(model)
    }
}