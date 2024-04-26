package com.example.myapplication.networking

import com.example.myapplication.networking.usersprofile.UserSchema
import retrofit2.http.GET
import retrofit2.http.Path

interface UserProfileApi {
    @GET("/{endpoint}")
    suspend fun fetchUsersProfile(@Path("endpoint") endpoint:String = "api/users/users.json"):List<UserSchema>
}