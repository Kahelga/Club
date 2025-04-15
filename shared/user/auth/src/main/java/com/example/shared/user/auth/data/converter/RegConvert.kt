package com.example.shared.user.auth.data.converter

import com.example.shared.user.auth.data.model.RegResponseData
import com.example.shared.user.auth.domain.entity.RegResponse

class RegConvert {
    fun convert(model: RegResponseData): RegResponse {
        return RegResponse(
            userId = model.userId,
            message = model.message
        )
    }
}