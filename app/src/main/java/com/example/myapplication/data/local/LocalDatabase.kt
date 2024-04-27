package com.example.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalDbEntry::class], version = 1, exportSchema = false)
abstract class LocalDatabase :RoomDatabase() {
    abstract val localDbDao: LocalDbDao
}