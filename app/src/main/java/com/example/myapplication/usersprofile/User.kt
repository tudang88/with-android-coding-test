package com.example.myapplication.usersprofile

import com.example.myapplication.networking.usersprofile.UserSchema


data class User(
    val id: Int,
    val nickname: String,
    val photo: String
)