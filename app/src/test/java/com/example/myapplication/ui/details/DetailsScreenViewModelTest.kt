package com.example.myapplication.ui.details

import com.example.myapplication.CustomInstantExecutorExtension
import com.example.myapplication.CustomTestRunnerExtension
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.FakeDataRepository
import io.kotest.core.coroutines.backgroundScope
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsScreenViewModelTest : FunSpec({
    extensions(CustomTestRunnerExtension(), CustomInstantExecutorExtension())
    // test target
    lateinit var detailsScreenViewModel: DetailsScreenViewModel    // use a fake repository to be injected into the viewmodel
    lateinit var dataRepository: FakeDataRepository

    // setup
    beforeTest {
        // init repository
        dataRepository = FakeDataRepository()
        detailsScreenViewModel = DetailsScreenViewModel(dataRepository)
    }

    test("selectUserById_showDetails").config(coroutineTestScope = true) {
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            detailsScreenViewModel.uiState.collect()
        }
        // Given: repository already have a list of USER
        // is SAMPLE_USERS
        // When: A user was specified by id
        detailsScreenViewModel.getUserDetails(SAMPLE_USERS[0].id)
        // Then: UI should be reflected the expected User
        val user = detailsScreenViewModel.uiState.value.user
        user shouldBe SAMPLE_USERS[0]

    }

    test("getShareEvent_updateFlag") {
        // Given: DetailScreen has already displayed
        // When: a share event come
        dataRepository.emitShareEvent(true)
        // Then: the event is transferred to viewModel layer
        val shareFlag = detailsScreenViewModel.shareEvent.first()
        shareFlag shouldBe true
        // clear event
        detailsScreenViewModel.clearShareEvent()
        val shareFlagClear = detailsScreenViewModel.shareEvent.first()
        shareFlagClear shouldBe false
    }

    test("onFavouriteChange").config(coroutineTestScope = true) {
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher()) {
            detailsScreenViewModel.uiState.collect()
        }
        // Given: A user has already displayed
        detailsScreenViewModel.getUserDetails(SAMPLE_USERS[0].id)
        // PART-1: mark as favourite
        // When: mark as favourite
        detailsScreenViewModel.onFavouriteChange(true)
        // Then: the change should be reflect on repository
        // And the state of DetailsScreen
        val isFav = detailsScreenViewModel.uiState.value.isFav
        val favList = dataRepository.getAllFavorites().first()
        favList.contains(SAMPLE_USERS[0].copy(isFavorite = true)) shouldBe true
        isFav shouldBe true
        // PART-2: un-mark favourite
        detailsScreenViewModel.onFavouriteChange(false)
        val favList2 = dataRepository.getAllFavorites().first()
        val isFav2 = detailsScreenViewModel.uiState.value.isFav
        favList2.contains(SAMPLE_USERS[0].copy(isFavorite = true)) shouldBe false
        isFav2 shouldBe false
    }
})
