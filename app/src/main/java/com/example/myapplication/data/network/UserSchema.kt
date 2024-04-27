package com.example.myapplication.data.network

import com.example.myapplication.data.local.LocalDbEntry
import com.example.myapplication.data.User
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
fun UserSchema.toDbEntry() = LocalDbEntry(id, nickname, photo)
fun List<UserSchema>.toDbEntries() = map(UserSchema::toDbEntry)