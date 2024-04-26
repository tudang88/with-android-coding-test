package com.example.myapplication.data.network

import com.example.myapplication.networking.UserProfileApi
import com.example.myapplication.networking.usersprofile.toLocal
import com.example.myapplication.usersprofile.User

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteNetworkDataSource @Inject constructor(
    private
    val networkApi: UserProfileApi
) : NetworkDataSource {
    override suspend fun getUsersProfile(): Flow<Result<List<User>>> {
        return withContext(Dispatchers.IO) {
            flow {
                try {
                    val result = networkApi.fetchUsersProfile()
                    emit(Result.success(result.map { it.toLocal() }))
                } catch (e: Exception) {
                    emit(Result.failure(e))
                }
            }
        }
    }

}
