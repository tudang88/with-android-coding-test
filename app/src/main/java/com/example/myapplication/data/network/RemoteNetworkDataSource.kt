package com.example.myapplication.data.network

import com.example.myapplication.networking.UserProfileApi

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class RemoteNetworkDataSource @Inject constructor(
    private
    val networkApi: UserProfileApi
) : NetworkDataSource {
    // A mutex is used to ensure that reads and writes are thread-safe.
    private val accessMutex = Mutex()

    /**
     * get all user data
     * from network
     */
    override suspend fun getUsersProfile(): List<UserSchema> = accessMutex.withLock {
        return networkApi.fetchUsersProfile()
    }
}
