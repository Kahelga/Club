package com.example.club.shared.tickets.domain.entity

import com.example.club.shared.user.profile.domain.entity.User

data class PurchaseRequest(
    val bookingId:String,
    val userId:String
   // val seats:List<String>,
    //val user: String,
)
