package com.example.club.hall.domain.usecase

import com.example.club.eventDetails.domain.repository.EventDetailsRepository
import com.example.club.hall.domain.entity.Hall
import com.example.club.hall.domain.repository.HallRepository

class GetHallUseCase(private val hallRepository: HallRepository){
    suspend operator fun invoke(eventId:String,token:String): Hall =
        hallRepository.get(eventId,token)
}