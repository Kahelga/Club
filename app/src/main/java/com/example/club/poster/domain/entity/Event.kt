package com.example.club.poster.domain.entity

data class Event (
    val id:String,
    val name:String,
    val date:String,
    val genres:List<String>,
    val ageRating:String,
    val minPrice:Int, //double
    val img:String
)

