package com.example.myapplication.ui.favourites

import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.FakeDataRepository
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavouritesScreenViewModelTest {
    // test target
    private lateinit var favouritesScreenViewModel: FavouritesScreenViewModel

    // use a fake repository to be injected into the viewmodel
    private lateinit var dataRepository: FakeDataRepository

    // set the main coroutines dispatcher for unit testing
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // init repository
        dataRepository = FakeDataRepository()
        favouritesScreenViewModel = FavouritesScreenViewModel(dataRepository)
    }

    @Test
    fun loadFavouritesScreen_showNoFavouriteItems() = runTest {
        // Given: No Items was marked as favourite
        // When: the FavouritesScreen is shown
        // Then: FavouritesScreen shows no items
        val favList = favouritesScreenViewModel.uiState.value.items
        assertThat(favList).hasSize(0)
    }

    @Test
    fun loadFavouritesScreen_showAllFavouriteItems() = runTest {
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
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
        assertThat(favList).hasSize(inputFavList.size)
        assertThat(favList).isEqualTo(inputFavList)

    }

    @Test
    fun changeFavourite_updateFavouritesList() = runTest {
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
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
        assertThat(favListAfter).hasSize(expectedList.size)
        assertThat(favListAfter).isEqualTo(expectedList)
    }
}