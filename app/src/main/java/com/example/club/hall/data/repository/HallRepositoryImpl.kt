package com.example.club.hall.data.repository

import com.example.club.hall.data.converter.HallConverter
import com.example.club.hall.data.network.HallApi
import com.example.club.hall.domain.entity.Hall
import com.example.club.hall.domain.repository.HallRepository

class HallRepositoryImpl(
    private val hallApi: HallApi,
    private val hallConverter: HallConverter
) :HallRepository{
    override suspend fun get(eventId: String,token:String): Hall{
        val model=hallApi.get(eventId,token)
        return hallConverter.convert(model)
    }
}