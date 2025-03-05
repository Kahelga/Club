package com.example.club.authorization.data.converter

import com.example.club.authorization.data.model.AuthResponseData
import com.example.club.authorization.data.model.RefreshTokenResponseData
import com.example.club.authorization.domain.entity.AuthResponse
import com.example.club.authorization.domain.entity.RefreshTokenResponse


class AuthConvert {
   fun convert(model: AuthResponseData): AuthResponse {
        return AuthResponse(

            accessToken = model.accessToken,
            refreshToken = model.refreshToken,
        )

   }
   fun convertToken(model:RefreshTokenResponseData):RefreshTokenResponse{
       return RefreshTokenResponse(
           accessToken = model.accessToken
       )
   }
}
