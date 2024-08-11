package com.example.myapplication.di

import android.app.Application
import androidx.room.Room
import com.example.myapplication.common.Constants
import com.example.myapplication.data.MyDataRepository
import com.example.myapplication.data.MyDataSource
import com.example.myapplication.data.local.LocalDatabase
import com.example.myapplication.data.local.LocalDbDao
import com.example.myapplication.data.network.NetworkDataSource
import com.example.myapplication.data.network.NetworkLogger
import com.example.myapplication.data.network.RemoteNetworkDataSource
import com.example.myapplication.networking.UserProfileApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindRepository(repository: MyDataRepository): MyDataSource
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkSourceModule {
    @Singleton
    @Binds
    abstract fun bindNetworkSource(networkSource: RemoteNetworkDataSource): NetworkDataSource
}

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
        val httpClient = OkHttpClient.Builder().addInterceptor(NetworkLogger()).build()
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
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
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
    fun localDbDao(database: LocalDatabase): LocalDbDao {
        return database.localDbDao
    }
}