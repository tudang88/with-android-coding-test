package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.data.User

@Entity(tableName = "user_table")
data class LocalDbEntry(
    @PrimaryKey
    val id: Int,
    val nickname: String,
    val photo: String,
    var isFavorite: Boolean = false
)

fun LocalDbEntry.toUser() = User(id,nickname,photo)
fun List<LocalDbEntry>.toUsers() = map(LocalDbEntry::toUser)