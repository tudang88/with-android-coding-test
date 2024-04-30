package com.example.myapplication.ui.home

import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.data.FakeDataRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeScreenViewModelTest {
    // test target
    private lateinit var homeScreenViewModel: HomeScreenViewModel

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
        homeScreenViewModel = HomeScreenViewModel(dataRepository)
    }

    @Test
    fun loadHomeScreen_showAllItems() = runTest {
        // Given: the app was started up
        // When: the HomeScreen is shown
        // Then: All items should be shown
        val items = homeScreenViewModel.uiState.value.items
        val itemsFromRepository = dataRepository.getAllCurrentItems().first().toList()
        assertThat(items).isEqualTo(itemsFromRepository)
    }

    @Test
    fun markItemAsFavourites_updateFavouritesList() = runTest {

        // Given: HomeScreen is shown
        val items = homeScreenViewModel.uiState.value.items
        // Part-1: mark favourite
        // When: mark the first Item as favourite by clicking favourite button
        homeScreenViewModel.onFavouriteChange(items[0].id, true)
        // Then: the favourite items list should be updated
        val favList1 = dataRepository.getAllFavorites().first().toList()
        assertThat(items[0].id).isEqualTo(favList1[0].id)
        // Part-2: un-mark favourite
        // when: remove favourite of the above item
        homeScreenViewModel.onFavouriteChange(items[0].id, false)
        val favList2 = dataRepository.getAllFavorites().first().toList()
        assertThat(favList2).hasSize(0)


    }
}