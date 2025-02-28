package com.example.club.authorization.data.converter

import com.example.club.authorization.data.model.AuthResponseData
import com.example.club.authorization.data.model.RefreshTokenResponseData
import com.example.club.authorization.domain.entity.AuthResponse
import com.example.club.authorization.domain.entity.RefreshTokenResponse


class AuthConvert {
   fun convert(model: AuthResponseData): AuthResponse {
        return AuthResponse(
       /*     success = model.success,
            reason = model.reason,
            user = model.user?.let { convertUser(it) },*/
            accessToken = model.accessToken,
            refreshToken = model.refreshToken,
            expiresIn = model.expiresIn
        )

   }
   fun convertToken(model:RefreshTokenResponseData):RefreshTokenResponse{
       return RefreshTokenResponse(
           accessToken = model.accessToken
       )
   }
}
/*private fun convertUser(userModel: UserModel): User {
       return User(
           phone = userModel.phone,
           firstname = userModel.firstname,
           middlename = userModel.middlename,
           lastname = userModel.lastname,
           email = userModel.email,
           city = userModel.city

       )
   }*/
/* private fun converterAuthInRequest(authRequestModel: AuthRequestModel):AuthRequest{
     return AuthRequest(
         email = authRequestModel.email,
         pass = authRequestModel.password
     )
 }*/