package com.example.myapplication.ui.details

import com.example.myapplication.MainCoroutineRule5
import com.example.myapplication.common.SAMPLE_USERS
import com.example.myapplication.data.FakeDataRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test


@ExperimentalCoroutinesApi
@ExtendWith(MainCoroutineRule5::class)
class DetailsScreenViewModelTest {
    // test target
    private lateinit var detailsScreenViewModel: DetailsScreenViewModel    // use a fake repository to be injected into the viewmodel
    private lateinit var dataRepository: FakeDataRepository

    @BeforeEach
    fun setUp() {
        // init repository
        dataRepository = FakeDataRepository()
        detailsScreenViewModel = DetailsScreenViewModel(dataRepository)
    }

    @Test
    fun selectUserById_showDetails() = runTest {
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            detailsScreenViewModel.uiState.collect()
        }
        // Given: repository already have a list of USER
        // is SAMPLE_USERS
        // When: A user was specified by id
        detailsScreenViewModel.getUserDetails(SAMPLE_USERS[0].id)
        // Then: UI should be reflected the expected User
        val user = detailsScreenViewModel.uiState.value.user
        assertThat(user).isEqualTo(SAMPLE_USERS[0])

    }

    @Test
    fun onFavouriteChange() = runTest {
        // Create an empty collector for the StateFlow
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
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
        assertThat(favList.contains(SAMPLE_USERS[0].copy(isFavorite = true))).isTrue()
        assertThat(isFav).isTrue()
        // PART-2: un-mark favourite
        detailsScreenViewModel.onFavouriteChange(false)
        val favList2 = dataRepository.getAllFavorites().first()
        val isFav2 = detailsScreenViewModel.uiState.value.isFav
        assertThat(favList2.contains(SAMPLE_USERS[0].copy(isFavorite = true))).isFalse()
        assertThat(isFav2).isFalse()
    }

    @Test
    fun getShareEvent_updateFlag() = runTest {
        // Given: DetailScreen has already displayed
        // When: a share event come
        dataRepository.emitShareEvent(true)
        // Then: the event is transferred to viewModel layer
        val shareFlag = detailsScreenViewModel.shareEvent.first()
        assertThat(shareFlag).isTrue()
        // clear event
        detailsScreenViewModel.clearShareEvent()
        val shareFlagClear = detailsScreenViewModel.shareEvent.first()
        assertThat(shareFlagClear).isFalse()
    }

}