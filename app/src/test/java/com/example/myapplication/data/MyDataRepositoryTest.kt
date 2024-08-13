package com.example.myapplication.data

import com.example.myapplication.common.SAMPLE_LOCAL_DATA
import com.example.myapplication.common.SAMPLE_NETWORK_DATA
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.local.FakeLocalDataSource
import com.example.myapplication.data.local.LocalDbDao
import com.example.myapplication.data.network.FakeRemoteDataSource
import com.example.myapplication.data.network.NetworkDataSource
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class MyDataRepositoryTest : FunSpec({
    // Test dependencies
    lateinit var networkDataSource: NetworkDataSource
    lateinit var localDataSource: LocalDbDao
    // target test module
    lateinit var dataRepository: MyDataRepository
    val testDispatcher = UnconfinedTestDispatcher()
    // Before test
    beforeTest {
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

    test("getAllItems_returnItemsAlreadyStoredInDatabase") {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA)
        // When: getAllItems
        val items = dataRepository.getAllCurrentItems().first()
        // Then:
        items.size shouldBe SAMPLE_LOCAL_DATA.size
    }

    test("getAllFavorites_someFavourites") {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA.map { it.copy(isFavorite = true) })
        // When: getAllFavorites
        val favList = dataRepository.getAllFavorites().first()
        // Then:
        favList.size shouldBe SAMPLE_LOCAL_DATA.size
    }

    test("getAllFavorites_noFavourites") {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA)
        // When: getAllFavorites
        val favList = dataRepository.getAllFavorites().first()
        // Then:
        favList.size shouldBe 0
    }

    test("markFavourite_updateFavouritesList") {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA)
        // When: mark first item as favourite
        dataRepository.markFavourite(SAMPLE_LOCAL_DATA[0].id, true)
        // Then:
        val favList = dataRepository.getAllFavorites().first()
        favList.size shouldBe 1
        // when: un-mark favourite
        dataRepository.markFavourite(SAMPLE_LOCAL_DATA[0].id, false)
        // Then:
        val favList2 = dataRepository.getAllFavorites().first()
        favList2.size shouldBe 0
    }

    test("getUserById_returnExpectedUserFromLocalDb") {
        // Given: database already have item
        localDataSource.insertAll(SAMPLE_LOCAL_DATA)
        // When: get one user by Id
        val user = dataRepository.getUserById(SAMPLE_USERS[0].id)
        // Then:
        user shouldBe SAMPLE_USERS[0]
    }

    test("clearDatabase_emptyDatabase") {
        // Given: refresh repository
        dataRepository.refresh()
        dataRepository.getAllCurrentItems().first().size shouldBe SAMPLE_LOCAL_DATA.size
        // when: clear data
        dataRepository.clearDatabase()
        // Then:
        dataRepository.getAllCurrentItems().first().size shouldBe 0
    }

    test("refreshData_ReloadFromNetWork_updateDatabase") {
        // Given: network source with SAMPLE_NETWORK_DATA
        networkDataSource.getUsersProfile().size shouldBe SAMPLE_NETWORK_DATA.size
        // And empty local database
        localDataSource.observeAll().first().size shouldBe 0
        dataRepository.getAllCurrentItems().first().size shouldBe 0
        // When: perform refresh
        dataRepository.refresh()
        // Then: all the data from network will be stored in local database
        localDataSource.observeAll().first().size shouldBe SAMPLE_NETWORK_DATA.size
        dataRepository.getAllCurrentItems().first().size shouldBe SAMPLE_NETWORK_DATA.size
    }

    test("emitShareEvent_collectResult") {
        // Given:
        // When : emit event
        dataRepository.emitShareEvent(true)
        val evenFlag = dataRepository.observeShareEven().first()
        evenFlag shouldBe true
        // when : clear event
        dataRepository.emitShareEvent(false)
        val evenFlag2 = dataRepository.observeShareEven().first()
        evenFlag2 shouldBe false
    }
})
