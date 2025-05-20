package com.example.club.shared.tickets.data.model

import com.example.club.shared.user.profile.data.model.UserModel
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseRequestModel(
    val bookingId:String,
    val userId:String
  //  val seats:List<String>,
   // val userId: String,
)
