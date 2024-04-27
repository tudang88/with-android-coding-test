package com.example.myapplication.networking

import com.example.myapplication.common.Constants
import com.example.myapplication.data.network.UserSchema
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit api interface
 */
interface UserProfileApi {
    @GET("{endpoint}")
    suspend fun fetchUsersProfile(@Path("endpoint") endpoint: String = Constants.USER_PROFILE_ENDPOINT): List<UserSchema>
}