package com.example.myapplication.usersprofile

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class User(
    val id: Int,
    val nickname: String,
    val photo: String
)