package com.example.myapplication.ui.master

import com.example.myapplication.CustomInstantExecutorExtension
import com.example.myapplication.CustomTestRunnerExtension
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.FakeDataRepository
import com.example.myapplication.ui.navigation.Screen
import io.kotest.core.coroutines.backgroundScope
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class MasterScreenViewModelTest : FunSpec({
    extensions(CustomTestRunnerExtension(), CustomInstantExecutorExtension())
    // test target
    lateinit var masterScreenViewModel: MasterScreenViewModel

    // use a fake repository to be injected into the viewmodel
    lateinit var dataRepository: FakeDataRepository

    beforeTest {
        // init repository
        dataRepository = FakeDataRepository()
        masterScreenViewModel = MasterScreenViewModel(dataRepository)
    }

    test("onScreenTransition_HomeToFavourite").config(coroutineTestScope = true) {
        // Given: the ui state was initialized
        // start from HomeScreen
        // When: transition to Favourite
        val targetScreen = Screen.FavouriteScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)

        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        appBarState shouldBe screenTransition(targetScreen)
    }

    test("onScreenTransition_HomeToDetails").config(coroutineTestScope = true) {
        // Given: the ui state was initialized
        // start from HomeScreen
        // When: transition to DetailsScreen
        val targetScreen = Screen.DetailsScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)

        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        appBarState shouldBe screenTransition(targetScreen)
    }

    test("onScreenTransition_FavouriteToDetails").config(coroutineTestScope = true) {
        // Given: the ui state was initialized
        // start from FavouriteScreen
        val startScreen = Screen.FavouriteScreen.route
        masterScreenViewModel.onScreenTransition(startScreen)

        // When: transition to DetailsScreen
        val targetScreen = Screen.DetailsScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)

        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        appBarState shouldBe screenTransition(targetScreen)
    }

    test("onScreenTransition_DetailsToHome").config(coroutineTestScope = true) {
        // Given: the ui state was initialized
        // start from Details
        val startScreen = Screen.DetailsScreen.route
        masterScreenViewModel.onScreenTransition(startScreen)
        // When: transition to HomeScreen
        val targetScreen = Screen.HomeScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)
        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        appBarState shouldBe screenTransition(targetScreen)
    }

    test("onScreenTransition_DetailsToFavourites").config(coroutineTestScope = true) {
        // Given: the ui state was initialized
        // start from Details
        val startScreen = Screen.DetailsScreen.route
        masterScreenViewModel.onScreenTransition(startScreen)
        // When: transition to Favourites
        val targetScreen = Screen.FavouriteScreen.route
        masterScreenViewModel.onScreenTransition(targetScreen)
        // Then: uiState should reflect the transition on it appBarState
        val appBarState = masterScreenViewModel.uiState.first().appBarState
        appBarState shouldBe screenTransition(targetScreen)
    }

    test("onTabSelected_Home").config(coroutineTestScope = true) {
        // Given: the ui state was initialized
        // start from HomeScreen
        // When: nothing
        // Then: tab index should be reflected on UiState
        val resultIndex = masterScreenViewModel.uiState.first().bottomTabIndex
        resultIndex shouldBe 0
    }

    test("onTabSelected_Favourites").config(coroutineTestScope = true) {
        // Given: the ui state was initialized
        // start from HomeScreen
        // When: select FavouriteTab
        masterScreenViewModel.onTabSelected(1)
        // Then: tab index should be reflected on UiState
        val resultIndex = masterScreenViewModel.uiState.first().bottomTabIndex
        resultIndex shouldBe 1
    }

    test("updateFavourites_changeFavouriteBadge").config(coroutineTestScope = true) {
        // Given: the ui state was initialized
        // start from HomeScreen with no favourite mark
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher()) {
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
        favCount1 shouldBe expected

        // Part -2
        // When: remove all favourites items
        for (item in SAMPLE_USERS) {
            dataRepository.markFavourite(item.id, false)
        }
        // Then: favourite Count should reflect the change from repository
        val favCount2 = masterScreenViewModel.uiState.value.favCount
        favCount2 shouldBe 0
    }
})
