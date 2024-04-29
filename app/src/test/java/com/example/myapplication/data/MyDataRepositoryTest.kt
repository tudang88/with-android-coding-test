package com.example.myapplication.data


import com.example.myapplication.common.SAMPLE_NETWORK_DATA
import com.example.myapplication.data.local.FakeLocalDataSource
import com.example.myapplication.data.local.LocalDbDao
import com.example.myapplication.data.network.FakeRemoteDataSource
import com.example.myapplication.data.network.NetworkDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MyDataRepositoryTest {
    // Test dependencies
    private lateinit var networkDataSource: NetworkDataSource
    private lateinit var localDataSource: LocalDbDao

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)

    // target test module
    private lateinit var taskRepository: MyDataRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        networkDataSource = FakeRemoteDataSource(SAMPLE_NETWORK_DATA.toMutableList())
        // start with empty database
        localDataSource = FakeLocalDataSource(listOf())
        // Get a reference to the class under test
        taskRepository = MyDataRepository(
            networkDatasource = networkDataSource,
            localDb = localDataSource,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun markFavourite() = testScope.runTest {
    }

    @Test
    fun getUserById() {
    }

    @Test
    fun getAllFavorites() {
    }

    @Test
    fun getAllCurrentItems() {
    }

    @Test
    fun emitShareEvent() {
    }

    @Test
    fun observeShareEven() {
    }

    @Test
    fun refresh() {
    }

    @Test
    fun clearDatabase() {
    }
}