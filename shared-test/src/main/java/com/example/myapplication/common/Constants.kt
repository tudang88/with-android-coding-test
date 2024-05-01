package com.example.myapplication.common

import androidx.annotation.VisibleForTesting
import com.example.myapplication.data.User
import com.example.myapplication.data.local.LocalDbEntry
import com.example.myapplication.data.network.UserSchema

@VisibleForTesting
val USER_PHOTOS = listOf(
    "https://raw.githubusercontent.com/withjp-inc/with_android_coding_test/main/assets/monster01.png",
    "https://raw.githubusercontent.com/withjp-inc/with_android_coding_test/main/assets/monster02.png",
    "https://raw.githubusercontent.com/withjp-inc/with_android_coding_test/main/assets/monster03.png",
    "https://raw.githubusercontent.com/withjp-inc/with_android_coding_test/main/assets/monster04.png",
    "https://raw.githubusercontent.com/withjp-inc/with_android_coding_test/main/assets/monster05.png"
)

@VisibleForTesting
val SAMPLE_USERS = listOf(
    User(
        id = 1,
        nickname = "User 1",
        photo = USER_PHOTOS[0]
    ),
    User(
        id = 2,
        nickname = "User 2",
        photo = USER_PHOTOS[1]
    ),
    User(
        id = 3,
        nickname = "User 3",
        photo = USER_PHOTOS[2]
    ),
    User(
        id = 4,
        nickname = "User 4",
        photo = USER_PHOTOS[3]
    ), User(
        id = 5,
        nickname = "User 5",
        photo = USER_PHOTOS[4]
    )
)

@VisibleForTesting
val SAMPLE_LOCAL_DATA = listOf(
    LocalDbEntry(
        id = 1,
        nickname = "User 1",
        photo = USER_PHOTOS[0]
    ),
    LocalDbEntry(
        id = 2,
        nickname = "User 2",
        photo = USER_PHOTOS[1]
    ),
    LocalDbEntry(
        id = 3,
        nickname = "User 3",
        photo = USER_PHOTOS[2]
    ),
    LocalDbEntry(
        id = 4,
        nickname = "User 4",
        photo = USER_PHOTOS[3]
    ),
    LocalDbEntry(
        id = 5,
        nickname = "User 5",
        photo = USER_PHOTOS[4]
    ),
)

@VisibleForTesting
val SAMPLE_NETWORK_DATA = listOf(
    UserSchema(
        id = 1,
        nickname = "User 1",
        photo = USER_PHOTOS[0]
    ),
    UserSchema(
        id = 2,
        nickname = "User 2",
        photo = USER_PHOTOS[1]
    ),
    UserSchema(
        id = 3,
        nickname = "User 3",
        photo = USER_PHOTOS[2]
    ),
    UserSchema(
        id = 4,
        nickname = "User 4",
        photo = USER_PHOTOS[3]
    ),
    UserSchema(
        id = 5,
        nickname = "User 5",
        photo = USER_PHOTOS[4]
    ),
)