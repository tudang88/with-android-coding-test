package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

/**
 * A fake repository for test
 */
class FakeDataRepository : MyDataSource {

    private val _shareEvent = MutableStateFlow(false)
    private val _userEntries = MutableStateFlow(LinkedHashMap<Int, User>())
    private val userEntries = listOf(
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
            photo = "PHOTO_URL4"
        ),
        User(
            id = 4,
            nickname = "User 4",
            photo = "PHOTO_URL1"
        ), User(
            id = 5,
            nickname = "User 5",
            photo = "PHOTO_URL5"
        )
    )

    init {
        for (item in userEntries) {
            _userEntries.value[item.id] = item
        }
    }

    /**
     * data action on favourite button
     */
    override suspend fun markFavourite(id: Int, isFav: Boolean) {
        val user = _userEntries.value[id]?.copy(isFavorite = isFav)
        if (user != null) {
            saveUser(user)
        }
    }

    /**
     * helper function for update a user
     */
    private fun saveUser(user: User) {
        _userEntries.update { entries ->
            val newUsers = LinkedHashMap<Int, User>(entries)
            newUsers[user.id] = user
            newUsers
        }
    }

    /**
     * obtain a user profile
     */
    override suspend fun getUserById(id: Int): User? {
        return _userEntries.value[id]
    }

    /**
     * get all favourites items
     */
    override fun getAllFavorites(): Flow<List<User>> {
        return flow {
            _userEntries.value.filter { entry -> entry.value.isFavorite }.toList()
        }
    }

    /**
     * Get all User items
     */
    override fun getAllCurrentItems(): Flow<List<User>> {
        return flow {
            _userEntries.value.map { entry -> entry.value }.toList()
        }
    }

    override fun emitShareEvent(value: Boolean) {
        _shareEvent.value = value
    }

    override fun observeShareEven(): Flow<Boolean> = _shareEvent

    override suspend fun refresh() {
        for (item in userEntries) {
            _userEntries.value[item.id] = item
        }
    }

    override suspend fun clearDatabase() {
        _userEntries.value.clear()
    }
}