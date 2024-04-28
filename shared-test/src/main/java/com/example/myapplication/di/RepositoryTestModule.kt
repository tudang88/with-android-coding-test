package com.example.myapplication.di

import com.example.myapplication.data.FakeDataRepository
import com.example.myapplication.data.MyDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


/**
 * Fake Repository for test
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
object RepositoryTestModule {
    @Singleton
    @Provides
    fun provideTasksRepository(): MyDataSource {
        return FakeDataRepository()
    }
}