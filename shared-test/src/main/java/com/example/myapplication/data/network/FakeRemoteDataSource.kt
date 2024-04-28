package com.example.myapplication.data.network

/**
 * Fake Data from remote Api
 */
class FakeRemoteDataSource(private val remoteUsers: MutableList<UserSchema>? = mutableListOf()): NetworkDataSource {
    override suspend fun getUsersProfile() : List<UserSchema> {
        return remoteUsers?.toList() ?: emptyList()
    }
}