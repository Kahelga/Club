package com.example.club.profile.data.converter



import com.example.club.profile.domain.entity.User
import com.example.club.profile.data.model.UserModel


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
    fun convert(model: UserModel): User {
        return User(
            id=model.id,
            phone = model.phone,
            firstname = model.firstname,
            middlename = model.middlename,
            lastname = model.lastname,
            email = model.email,
            city = model.city


        )

    }
}