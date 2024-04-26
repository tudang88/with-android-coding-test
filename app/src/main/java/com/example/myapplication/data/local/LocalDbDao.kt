package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDbDao {
    /**
     * insert one item
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(favProfile: LocalDbEntry)

    /**
     * insert multiple items
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg items: LocalDbEntry)

    /**
     * observe database state
     * via flow
     */
    @Query("SELECT * FROM user_table")
    fun observe(): Flow<List<LocalDbEntry>>

    /**
     * get an item by id
     */
    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getById(id: String): LocalDbEntry?

    /**
     * observe all favorite items via flow
     */
    @Query("SELECT * FROM user_table WHERE isFavorite = 1")
    fun getFavoriteItems(): Flow<List<LocalDbEntry>>

    /**
     * observe a specific item
     */
    @Query("SELECT * FROM user_table WHERE id = :id")
    fun observeById(id: String): Flow<LocalDbEntry?>

    /**
     * delete one item
     */
    @Query("DELETE FROM user_table WHERE id = :id")
    suspend fun delete(id: String)

    /**
     * clear all db
     */
    @Query("DELETE FROM user_table")
    suspend fun clearDatabase()
}