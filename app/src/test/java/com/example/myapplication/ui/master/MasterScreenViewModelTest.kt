package com.example.myapplication.ui.master

import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.FakeDataRepository
import com.example.myapplication.ui.navigation.Screen
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class MasterScreenViewModelTest {
    // test target
    private lateinit var masterScreenViewModel: MasterScreenViewModel

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
        masterScreenViewModel = MasterScreenViewModel(dataRepository)
    }

    @Test
    fun onScreenTransition_HomeToFavourite() = runTest {
        // Given: the ui state was initialized
        // start from HomeScreen
        // When: transition to Favourite
        val targetScreen = Screen.FavouriteScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)

        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        assertThat(appBarState).isEqualTo(appBarState.screenTransition(targetScreen))
    }

    @Test
    fun onScreenTransition_HomeToDetails() = runTest {
        // Given: the ui state was initialized
        // start from HomeScreen
        // When: transition to DetailsScreen
        val targetScreen = Screen.DetailsScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)

        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        assertThat(appBarState).isEqualTo(appBarState.screenTransition(targetScreen))
    }

    @Test
    fun onScreenTransition_FavouriteToDetails() = runTest {
        // Given: the ui state was initialized
        // start from FavouriteScreen
        val startScreen = Screen.FavouriteScreen.route
        masterScreenViewModel.onScreenTransition(startScreen)

        // When: transition to DetailsScreen
        val targetScreen = Screen.DetailsScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)

        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        assertThat(appBarState).isEqualTo(appBarState.screenTransition(targetScreen))
    }

    @Test
    fun onScreenTransition_DetailsToHome() = runTest {
        // Given: the ui state was initialized
        // start from Details
        val startScreen = Screen.DetailsScreen.route
        masterScreenViewModel.onScreenTransition(startScreen)
        // When: transition to HomeScreen
        val targetScreen = Screen.HomeScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)
        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        assertThat(appBarState).isEqualTo(appBarState.screenTransition(targetScreen))
    }

    @Test
    fun onScreenTransition_DetailsToFavourites() = runTest {
        // Given: the ui state was initialized
        // start from Details
        val startScreen = Screen.DetailsScreen.route
        masterScreenViewModel.onScreenTransition(startScreen)
        // When: transition to Favourites
        val targetScreen = Screen.FavouriteScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)
        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        assertThat(appBarState).isEqualTo(appBarState.screenTransition(targetScreen))
    }

    @Test
    fun onTabSelected_Home() = runTest {
        // Given: the ui state was initialized
        // start from HomeScreen
        // When: nothing
        // Then: tab index should be reflected on UiState
        val resultIndex = masterScreenViewModel.uiState.first().bottomTabIndex
        assertThat(resultIndex).isEqualTo(0)
    }

    @Test
    fun onTabSelected_Favourites() = runTest {
        // Given: the ui state was initialized
        // start from HomeScreen
        // When: select FavouriteTab
        masterScreenViewModel.onTabSelected(1)
        // Then: tab index should be reflected on UiState
        val resultIndex = masterScreenViewModel.uiState.first().bottomTabIndex
        assertThat(resultIndex).isEqualTo(1)
    }

    @Test
    fun updateFavourites_changeFavouriteBadge() = runTest {
        // Given: the ui state was initialized
        // start from HomeScreen with no favourite mark
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            masterScreenViewModel.uiState.collect()
        }
        // Part -1
        // When: mark all item to favourite
        val expected = SAMPLE_USERS.size
        for (item in SAMPLE_USERS) {
            dataRepository.markFavourite(item.id, true)
        }
        // Then: favourite Count should reflect the change from repository
        val favCount1 = masterScreenViewModel.uiState.value.favCount
        assertThat(favCount1).isEqualTo(expected)

        // Part -2
        // When: remove all favourites items
        for (item in SAMPLE_USERS) {
            dataRepository.markFavourite(item.id, false)
        }
        // Then: favourite Count should reflect the change from repository
        val favCount2 = masterScreenViewModel.uiState.value.favCount
        assertThat(favCount2).isEqualTo(0)
    }
}