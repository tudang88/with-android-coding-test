package com.example.myapplication.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.example.myapplication.common.SAMPLE_USERS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * A fake repository for test
 */
class FakeDataRepository : MyDataSource {

    private val _shareEvent = MutableStateFlow(false)
    private val _userEntries = MutableStateFlow(LinkedHashMap<Int, User>())

    @SuppressLint("VisibleForTests")
    private val userEntries = SAMPLE_USERS

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
        return _userEntries.map { userMap ->
            userMap.values.filter { it.isFavorite }
        }
    }

    /**
     * Get all User items
     */
    override fun getAllCurrentItems(): Flow<List<User>> {
        return _userEntries.map { userMap ->
            userMap.values.toList()
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

    /**
     * this method only use for testing
     */
    @VisibleForTesting
    fun addUsersData(users: List<User>) {
        _userEntries.update { oldEntries ->
            val newEntries = LinkedHashMap<Int, User>(oldEntries)
            for (item in users) {
                newEntries[item.id] = item
            }
            newEntries
        }
    }
}