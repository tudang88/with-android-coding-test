package com.example.myapplication.ui.favourites

import com.example.myapplication.CustomInstantExecutorExtension
import com.example.myapplication.CustomTestRunnerExtension
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.FakeDataRepository
import io.kotest.core.coroutines.backgroundScope
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class FavouritesScreenViewModelTest : FunSpec({
    extensions(CustomTestRunnerExtension(), CustomInstantExecutorExtension())
    // test target
    lateinit var favouritesScreenViewModel: FavouritesScreenViewModel

    // use a fake repository to be injected into the viewmodel
    lateinit var dataRepository: FakeDataRepository

    // setup
    beforeTest {
        // init repository
        dataRepository = FakeDataRepository()
        favouritesScreenViewModel = FavouritesScreenViewModel(dataRepository)
    }

    test("loadFavouritesScreen_showNoFavouriteItems").config(coroutineTestScope = true) {
        // Given: No Items was marked as favourite
        // When: the FavouritesScreen is shown
        // Then: FavouritesScreen shows no items
        val favList = favouritesScreenViewModel.uiState.value.items
        favList.size shouldBe 0
    }

    test("loadFavouritesScreen_showAllFavouriteItems").config(coroutineTestScope = true) {
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            favouritesScreenViewModel.uiState.collect()
        }
        // Given: Some items were marked as favourite
        val inputFavList = SAMPLE_USERS.map {
            it.copy(isFavorite = true)
        }
        dataRepository.clearDatabase()
        dataRepository.addUsersData(inputFavList)
        // When: the FavouritesScreen is shown
        // Then: FavouritesScreen shows all items
        val favList = favouritesScreenViewModel.uiState.value.items
        favList.size shouldBe inputFavList.size
        favList shouldBe inputFavList
    }

    test("changeFavourite_updateFavouritesList").config(coroutineTestScope = true) {
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            favouritesScreenViewModel.uiState.collect()
        }
        // Given: Some items were marked as favourite
        val inputFavList = SAMPLE_USERS.map {
            it.copy(isFavorite = true)
        }
        dataRepository.clearDatabase()
        dataRepository.addUsersData(inputFavList)

        // When: un-mark the first item
        val favListBefore = favouritesScreenViewModel.uiState.value.items
        favouritesScreenViewModel.onFavouriteChange(favListBefore[0].id, false)
        val expectedList =
            favListBefore.drop(1)
        // Then: FavouritesScreen
        val favListAfter = favouritesScreenViewModel.uiState.value.items
        favListAfter.size shouldBe expectedList.size
        favListAfter shouldBe expectedList
    }
})
