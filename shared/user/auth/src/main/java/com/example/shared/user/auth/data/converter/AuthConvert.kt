package com.example.shared.user.auth.data.converter

import com.example.shared.user.auth.data.model.AuthResponseData
import com.example.shared.user.auth.data.model.RefreshTokenResponseData
import com.example.shared.user.auth.domain.entity.AuthResponse
import com.example.shared.user.auth.domain.entity.RefreshTokenResponse


class AuthConvert {
   fun convert(model: AuthResponseData): AuthResponse {
        return AuthResponse(

            accessToken = model.accessToken,
            refreshToken = model.refreshToken,
            role=model.role
        )

   }
   fun convertToken(model: RefreshTokenResponseData): RefreshTokenResponse {
       return RefreshTokenResponse(
           accessToken = model.accessToken
       )
   }
}
