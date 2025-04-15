package com.example.club.shared.event.data.repository

import com.example.club.shared.event.data.converter.EventDetailsConverter
import com.example.club.shared.event.data.network.EventDetailsAdminApi
import com.example.club.shared.event.domain.entity.EventDetails
import com.example.club.shared.event.domain.repository.EventDetailsAdminRepository


class EventAdminRepositoryImpl(
    private val eventDetailsAdminApi: EventDetailsAdminApi,
    private val eventDetailsConverter: EventDetailsConverter
) : EventDetailsAdminRepository {
    override suspend fun get(token:String,eventId: String): EventDetails {
        val model =  eventDetailsAdminApi.get(token,eventId)
        return  eventDetailsConverter.convert(model)
    }
}