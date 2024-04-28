package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * setup temporary in memory
 * db for test
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseTestModule {
    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): LocalDatabase {
        return Room
            .inMemoryDatabaseBuilder(context.applicationContext, LocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}