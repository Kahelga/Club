package com.example.club.eventDetails.data.converter

import com.example.club.eventDetails.data.EventDetailsResponseData
import com.example.club.eventDetails.data.model.EventArtistsModel
import com.example.club.eventDetails.data.model.EventVenueModel
import com.example.club.eventDetails.domain.entity.EventArtists
import com.example.club.eventDetails.domain.entity.EventDetails
import com.example.club.eventDetails.domain.entity.EventVenue

class EventDetailsConverter {
    private fun convertArtist(eventArtistsModel: EventArtistsModel):EventArtists{
        return EventArtists(
            id=eventArtistsModel.id,
            professions = eventArtistsModel.professions,
            fullName = eventArtistsModel.fullName
        )
    }
    private fun convertVenue(eventVenueModel: EventVenueModel): EventVenue {
        return EventVenue(
            name = eventVenueModel.name,
            capacity = eventVenueModel.capacity
        )
    }
   fun convert(model:EventDetailsResponseData):EventDetails{
       return EventDetails(
           id = model.event.id,
           name = model.event.name,
           description = model.event.description,
           date = model.event.date,
           time = model.event.time,
           artists = model.event.artists.map { convertArtist(it) },
           venue = convertVenue(model.event.venue),
           duration = model.event.duration,
           ageRating = model.event.ageRating,
           genres = model.event.genres,
           img=model.event.img,
           minPrice = model.event.minPrice
       )
   }
}