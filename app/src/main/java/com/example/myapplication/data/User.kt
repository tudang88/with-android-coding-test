package com.example.myapplication.data

import com.example.myapplication.data.local.LocalDbEntry


data class User(
    val id: Int,
    val nickname: String,
    val photo: String,
    val isFavorite: Boolean = false
)

fun User.toDbEntry() = LocalDbEntry(id, nickname, photo, isFavorite)