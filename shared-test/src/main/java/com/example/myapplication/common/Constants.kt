package com.example.myapplication.common

import androidx.annotation.VisibleForTesting
import com.example.myapplication.data.User

@VisibleForTesting
val SAMPLE_USERS = listOf(
    User(
        id = 1,
        nickname = "User 1",
        photo = "PHOTO_URL1"
    ),
    User(
        id = 2,
        nickname = "User 2",
        photo = "PHOTO_URL2"
    ),
    User(
        id = 3,
        nickname = "User 3",
        photo = "PHOTO_URL3"
    ),
    User(
        id = 4,
        nickname = "User 4",
        photo = "PHOTO_URL4"
    ), User(
        id = 5,
        nickname = "User 5",
        photo = "PHOTO_URL5"
    )
)