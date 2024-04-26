package com.example.myapplication.data

import com.example.myapplication.data.local.LocalDbDao
import com.example.myapplication.data.network.NetworkDataSource
import com.example.myapplication.di.ApplicationScope
import com.example.myapplication.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyDataRepository @Inject constructor(
    private val networkDatasource: NetworkDataSource,
    private val localDb: LocalDbDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : MyDataSource {
    override suspend fun markFavourite(id: Int, isFav: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFavorites() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCurrentItems() {
        TODO("Not yet implemented")
    }
}