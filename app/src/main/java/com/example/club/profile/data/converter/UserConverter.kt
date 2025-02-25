package com.example.club.profile.data.converter


import com.example.club.authorization.data.model.UserModel
import com.example.club.profile.domain.entity.User
import com.example.club.profile.data.ProfileResponseData


class UserConverter {
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
    fun convert(model: ProfileResponseData): User {
        return User(
            id=model.user.id,
            phone = model.user.phone,
            firstname = model.user.firstname,
            middlename = model.user.middlename,
            lastname = model.user.lastname,
            email = model.user.email,
            city = model.user.city


        )

    }
}