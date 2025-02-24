package com.example.club.poster.data.converter

import com.example.club.poster.data.EventResponseData
import com.example.club.poster.data.model.EventModel
import com.example.club.poster.domain.EventResponse
import com.example.club.poster.domain.entity.Event


class EventPosterConverter {
    private fun convertEvent(eventModel: EventModel): Event {
        return Event(
            id = eventModel.id,
            name = eventModel.name,
            date = eventModel.date,
            genres = eventModel.genres,
            ageRating = eventModel.ageRating,
            minPrice = eventModel.minPrice,
            img = eventModel.img

        )
    }

    fun convert(model: EventResponseData): EventResponse =
        EventResponse(
            success = model.success,
            reason = model.reason,
            events = model.events.map { convertEvent(it) }
        )
}