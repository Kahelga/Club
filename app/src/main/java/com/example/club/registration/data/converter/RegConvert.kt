package com.example.club.registration.data.converter

import com.example.club.registration.data.model.RegResponseData
import com.example.club.registration.domain.entity.RegResponse

class RegConvert {
    fun convert(model: RegResponseData):RegResponse{
        return RegResponse(
            userId = model.userId,
            message = model.message
        )
    }
}