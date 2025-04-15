package com.example.club.shared.event.domain.usecase


import com.example.club.shared.event.domain.entity.Hall
import com.example.club.shared.event.domain.repository.HallRepository

class GetHallUseCase(private val hallRepository: HallRepository){
    suspend operator fun invoke(eventId:String,token:String): Hall =
        hallRepository.get(eventId,token)
}