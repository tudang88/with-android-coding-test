package com.example.myapplication.networking.usersprofile

import com.example.myapplication.usersprofile.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSchema(
    val id: Int,
    val nickname: String,
    val photo: String
)

/**
 * convert Network model
 * to domain model
 */
fun UserSchema.toLocal() = User(id, nickname, photo)