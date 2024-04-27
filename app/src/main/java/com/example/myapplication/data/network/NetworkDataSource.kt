package com.example.myapplication.data.network

interface NetworkDataSource {
    suspend fun getUsersProfile(): List<UserSchema>
}