package com.example.club.shared.event.data.converter


import com.example.club.shared.event.data.model.EventModel
import com.example.club.shared.event.domain.entity.Event


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