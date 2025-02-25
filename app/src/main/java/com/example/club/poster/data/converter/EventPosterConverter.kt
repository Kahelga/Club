package com.example.club.poster.data.converter

import com.example.club.poster.data.EventResponseData
import com.example.club.poster.data.model.EventModel
import com.example.club.poster.domain.EventResponse
import com.example.club.poster.domain.entity.Event


class EventPosterConverter {
    /*private*/ fun convert(eventModel: EventModel): Event {
        return Event(
            id = eventModel.id,
            title = eventModel.title,
            date = eventModel.date,
            genre = eventModel.genre,
            ageRating = eventModel.ageRating,
            minPrice = eventModel.minPrice,
            imgPreview = eventModel.imgPreview,
            status = eventModel.status

        )
    }

    /*fun convert(model: EventResponseData): EventResponse =
        EventResponse(
            success = model.success,
            reason = model.reason,
            events = model.events.map { convertEvent(it) }
        )*/
}