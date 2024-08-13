package com.example.myapplication.ui.home

import com.example.myapplication.data.FakeDataRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest : FunSpec({
    // test target
    lateinit var homeScreenViewModel: HomeScreenViewModel

    // use a fake repository to be injected into the viewmodel
    lateinit var dataRepository: FakeDataRepository

    // setup
    beforeTest {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // init repository
        dataRepository = FakeDataRepository()
        homeScreenViewModel = HomeScreenViewModel(dataRepository)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    test("loadHomeScreen_showAllItems").config(coroutineTestScope = true) {
        // Given: the app was started up
        // When: the HomeScreen is shown
        // Then: All items should be shown
        val items = homeScreenViewModel.uiState.value.items
        val itemsFromRepository = dataRepository.getAllCurrentItems().first().toList()
        items shouldBe itemsFromRepository
    }

    test("markItemAsFavourites_updateFavouritesList").config(coroutineTestScope = true) {

        // Given: HomeScreen is shown
        val items = homeScreenViewModel.uiState.value.items
        // Part-1: mark favourite
        // When: mark the first Item as favourite by clicking favourite button
        homeScreenViewModel.onFavouriteChange(items[0].id, true)
        // Then: the favourite items list should be updated
        val favList1 = dataRepository.getAllFavorites().first().toList()
        items[0].id shouldBe favList1[0].id
        // Part-2: un-mark favourite
        // when: remove favourite of the above item
        homeScreenViewModel.onFavouriteChange(items[0].id, false)
        val favList2 = dataRepository.getAllFavorites().first().toList()
        favList2.size shouldBe 0
    }
})
