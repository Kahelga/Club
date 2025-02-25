package com.example.club.eventDetails.data.converter

import com.example.club.eventDetails.data.EventDetailsResponseData
import com.example.club.eventDetails.data.model.EventArtistsModel
import com.example.club.eventDetails.data.model.EventDetailsModel
import com.example.club.eventDetails.data.model.EventVenueModel
import com.example.club.eventDetails.domain.entity.EventArtists
import com.example.club.eventDetails.domain.entity.EventDetails
import com.example.club.eventDetails.domain.entity.EventVenue

class EventDetailsConverter {
    private fun convertArtist(eventArtistsModel: EventArtistsModel):EventArtists{
        return EventArtists(
            id=eventArtistsModel.id,
            name = eventArtistsModel.name,
            profession = eventArtistsModel.profession

        )
    }
    /*private fun convertVenue(eventVenueModel: EventVenueModel): EventVenue {
        return EventVenue(
            name = eventVenueModel.name,
            capacity = eventVenueModel.capacity
        )
    }*/
   fun convert(model:EventDetailsModel/*EventDetailsResponseData*/):EventDetails{
       return EventDetails(
           id = model.id,
           title = model.title,
           date = model.date,
           genre = model.genre,
           ageRating = model.ageRating,
           description = model.description,
           artists = model.artists.map {convertArtist(it)},
           duration = model.duration,
           imgPreview=model.imgPreview,
           minPrice = model.minPrice,
           status = model.status
       )
   }
}