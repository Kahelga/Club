package com.example.club.eventDetails.data.repository

import com.example.club.eventDetails.data.converter.EventDetailsConverter
import com.example.club.eventDetails.data.network.EventDetailsApi
import com.example.club.eventDetails.domain.entity.EventDetails
import com.example.club.eventDetails.domain.repository.EventDetailsRepository

class EventRepositoryImpl(
    private val eventDetailsApi: EventDetailsApi,
    private val eventDetailsConverter: EventDetailsConverter
) :EventDetailsRepository{
    override suspend fun get(eventId: String): EventDetails {
        val model =  eventDetailsApi.get(eventId)
        return  eventDetailsConverter.convert(model)
    }
}