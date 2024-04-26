package com.example.myapplication.data.network

import com.example.myapplication.usersprofile.User
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {
    suspend fun getUsersProfile(): Flow<Result<List<User>>>
}