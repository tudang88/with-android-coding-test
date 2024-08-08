package com.example.myapplication.data

import com.example.myapplication.MainCoroutineRule5
import com.example.myapplication.common.SAMPLE_LOCAL_DATA
import com.example.myapplication.common.SAMPLE_NETWORK_DATA
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.local.FakeLocalDataSource
import com.example.myapplication.data.local.LocalDbDao
import com.example.myapplication.data.network.FakeRemoteDataSource
import com.example.myapplication.data.network.NetworkDataSource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
@ExtendWith(MainCoroutineRule5::class)
class MyDataRepositoryTest {

    // Test dependencies
    private lateinit var networkDataSource: NetworkDataSource
    private lateinit var localDataSource: LocalDbDao

    private var testDispatcher = UnconfinedTestDispatcher()

    // set the main coroutines dispatcher for unit testing
    @JvmField
    @RegisterExtension
    val mainCoroutineRule = MainCoroutineRule5()

    // target test module
    private lateinit var dataRepository: MyDataRepository

    @BeforeEach
    fun setUp() {
        networkDataSource = FakeRemoteDataSource(SAMPLE_NETWORK_DATA.toMutableList())
        // start with empty database
        localDataSource = FakeLocalDataSource(listOf())
        // Get a reference to the class under test
        dataRepository = MyDataRepository(
            networkDatasource = networkDataSource,
            localDb = localDataSource,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun refreshData_ReloadFromNetWork_updateDatabase() = runTest {
        // Given: network source with SAMPLE_NETWORK_DATA
        assertThat(networkDataSource.getUsersProfile()).hasSize(SAMPLE_NETWORK_DATA.size)
        // And empty local database
        assertThat(localDataSource.observeAll().first()).hasSize(0)
        assertThat(dataRepository.getAllCurrentItems().first()).hasSize(0)
        // When: perform refresh
        dataRepository.refresh()
        // Then: all the data from network will be stored in local database
        assertThat(localDataSource.observeAll().first()).hasSize(SAMPLE_NETWORK_DATA.size)
        assertThat(dataRepository.getAllCurrentItems().first()).hasSize(SAMPLE_NETWORK_DATA.size)
    }

    @Test
    fun getAllItems_returnItemsAlreadyStoredInDatabase() = runTest {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA)
        // When: getAllItems
        val items = dataRepository.getAllCurrentItems().first()
        // Then:
        assertThat(items).hasSize(SAMPLE_LOCAL_DATA.size)
    }

    @Test
    fun getAllFavorites_someFavourites() = runTest {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA.map { it.copy(isFavorite = true) })
        // When: getAllFavorites
        val favList = dataRepository.getAllFavorites().first()
        // Then:
        assertThat(favList).hasSize(SAMPLE_LOCAL_DATA.size)
    }

    @Test
    fun getAllFavorites_noFavourites() = runTest {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA)
        // When: getAllFavorites
        val favList = dataRepository.getAllFavorites().first()
        // Then:
        assertThat(favList).hasSize(0)
    }

    @Test
    fun markFavourite_updateFavouritesList() = runTest {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA)
        // When: mark first item as favourite
        dataRepository.markFavourite(SAMPLE_LOCAL_DATA[0].id, true)
        // Then:
        val favList = dataRepository.getAllFavorites().first()
        assertThat(favList).hasSize(1)
        // when: un-mark favourite
        dataRepository.markFavourite(SAMPLE_LOCAL_DATA[0].id, false)
        // Then:
        val favList2 = dataRepository.getAllFavorites().first()
        assertThat(favList2).hasSize(0)
    }

    @Test
    fun getUserById_returnExpectedUserFromLocalDb() = runTest {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA)
        // When: get one user by Id
        val user = dataRepository.getUserById(SAMPLE_USERS[0].id)
        // Then:
        assertThat(user).isEqualTo(SAMPLE_USERS[0])
    }

    @Test
    fun clearDatabase_emptyDatabase() = runTest {
        // Given: refresh repository
        dataRepository.refresh()
        assertThat(dataRepository.getAllCurrentItems().first()).hasSize(SAMPLE_LOCAL_DATA.size)
        // when: clear data
        dataRepository.clearDatabase()
        // Then:
        assertThat(dataRepository.getAllCurrentItems().first()).hasSize(0)
    }

    @Test
    fun emitShareEvent_collectResult() = runTest {
        // Given:
        // When : emit event
        dataRepository.emitShareEvent(true)
        val evenFlag = dataRepository.observeShareEven().first()
        assertThat(evenFlag).isTrue()
        // when : clear event
        dataRepository.emitShareEvent(false)
        val evenFlag2 = dataRepository.observeShareEven().first()
        assertThat(evenFlag2).isFalse()
    }
}
