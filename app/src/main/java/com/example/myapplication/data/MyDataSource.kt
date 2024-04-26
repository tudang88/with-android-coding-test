package com.example.myapplication.data

interface MyDataSource {

    /**
     * method for add a favorite
     * isFav: True -> mark as fav
     * isFav: False -> remove fav mark
     */
    suspend fun markFavourite(id: Int, isFav: Boolean)

    /**
     * getFavorites item from DB
     */
    suspend fun getAllFavorites()

    /**
     * get all items available in localDb
     * use for home screen
     */
    suspend fun getAllCurrentItems()
}