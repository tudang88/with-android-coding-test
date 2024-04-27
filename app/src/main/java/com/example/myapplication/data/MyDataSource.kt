package com.example.myapplication.data

import kotlinx.coroutines.flow.Flow

interface MyDataSource {

    /**
     * method for add a favorite
     * isFav: True -> mark as fav
     * isFav: False -> remove fav mark
     */
    suspend fun markFavourite(id: Int, isFav: Boolean)

    /**
     * get User info by specifying id
     * normally the user data will be retrieved
     * from database
     */
    suspend fun getUserById(id: Int): User?
    /**
     * getFavorites item from DB
     */
    fun getAllFavorites(): Flow<List<User>>

    /**
     * get all items available in localDb
     * use for home screen
     */
    fun getAllCurrentItems(): Flow<List<User>>

    /**
     * pass event from top layer
     * to lower layer
     */
    fun emitShareEvent(value :Boolean)

    /**
     * observe share event action button
     */
    fun observeShareEven(): Flow<Boolean>
    /**
     * refresh data and update database
     */
    suspend fun refresh()

    /**
     * clear all database
     */
    suspend fun clearDatabase()
}