package com.example.myapplication.data

import com.example.myapplication.data.local.LocalDbDao
import com.example.myapplication.data.local.toUser
import com.example.myapplication.data.local.toUsers
import com.example.myapplication.data.network.NetworkDataSource
import com.example.myapplication.data.network.toDbEntries
import com.example.myapplication.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyDataRepository @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val localDb: LocalDbDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : MyDataSource {
    private val _shareEvent = MutableStateFlow(false)

    /**
     * mark one favorite
     */
    override suspend fun markFavourite(id: Int, isFav: Boolean) {
        withContext(dispatcher) {
            val user = localDb.getById(id)?.copy(isFavorite = isFav)
            if (user != null) {
                localDb.upsert(user)
            }
        }
    }

    /**
     * get one user profile
     */
    override suspend fun getUserById(id: Int): User? {
        return withContext(dispatcher) {
            localDb.getById(id)?.toUser()
        }
    }

    /**
     * observe all favorite entries
     */
    override fun getAllFavorites(): Flow<List<User>> {
        return localDb.getFavoriteItems().map { entry ->
            withContext(dispatcher) {
                entry.toUsers()
            }
        }
    }

    /**
     * observer database
     */
    override fun getAllCurrentItems(): Flow<List<User>> {
        return localDb.observeAll().map { entry ->
            withContext(dispatcher) {
                entry.toUsers()
            }
        }
    }

    override fun emitShareEvent(value: Boolean) {
        _shareEvent.value = value
    }

    override fun observeShareEven(): Flow<Boolean> = _shareEvent

    /**
     * get data from network
     * and update database
     */
    override suspend fun refresh() {
        withContext(dispatcher) {
            val remoteData = networkDatasource.getUsersProfile()
            localDb.clearDatabase()
            localDb.insertAll(remoteData.toDbEntries())
        }
    }

    /**
     * clear database
     */
    override suspend fun clearDatabase() {
        withContext(dispatcher) {
            localDb.clearDatabase()
        }
    }
}