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
    suspend fun insertAll(items: List<LocalDbEntry>)

    /**
     * observe database state
     * via flow
     */
    @Query("SELECT * FROM user_table")
    fun observeAll(): Flow<List<LocalDbEntry>>

    /**
     * get an item by id
     */
    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getById(id: Int): LocalDbEntry?

    /**
     * observe all favorite items via flow
     */
    @Query("SELECT * FROM user_table WHERE isFavorite = 1")
    fun getFavoriteItems(): Flow<List<LocalDbEntry>>

    /**
     * observe a specific item
     */
    @Query("SELECT * FROM user_table WHERE id = :id")
    fun observeById(id: Int): Flow<LocalDbEntry?>

    /**
     * delete one item
     */
    @Query("DELETE FROM user_table WHERE id = :id")
    suspend fun delete(id: Int)

    /**
     * clear all db
     */
    @Query("DELETE FROM user_table")
    suspend fun clearDatabase()
}