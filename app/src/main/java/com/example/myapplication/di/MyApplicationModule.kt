package com.example.myapplication.di

import android.app.Application
import androidx.room.Room
import com.example.myapplication.common.Constants
import com.example.myapplication.data.local.LocalDatabase
import com.example.myapplication.data.local.LocalDbDao
import com.example.myapplication.networking.UserProfileApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Construction module
 * for DI
 */
@Module
@InstallIn(SingletonComponent::class)
class MyApplicationModule {
    @Provides
    @Singleton
    fun retrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    fun userProfileApi(retrofit: Retrofit): UserProfileApi {
        return retrofit.create(UserProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun myRoomDatabase(application: Application): LocalDatabase {
        return Room.databaseBuilder(
            application,
            LocalDatabase::class.java,
            Constants.DB_NAME
        ).build()
    }

    @Provides
    fun favouriteDao(database: LocalDatabase): LocalDbDao {
        return database.favouriteDao
    }
}