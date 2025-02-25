package com.example.club.authorization.data.converter

import com.example.club.authorization.data.AuthResponseData
import com.example.club.authorization.data.model.AuthRequestModel
import com.example.club.authorization.domain.AuthResponse
import com.example.club.authorization.domain.entity.AuthRequest



class UserConvert {
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
   fun convert(model:AuthResponseData): AuthResponse {
        return AuthResponse(
       /*     success = model.success,
            reason = model.reason,
            user = model.user?.let { convertUser(it) },*/
            accessToken = model.accessToken,
            refreshToken = model.refreshToken
        )

    }
}