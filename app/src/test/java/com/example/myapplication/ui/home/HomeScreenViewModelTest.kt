package com.example.myapplication.ui.home

import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.data.FakeDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

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

    fun onFavouriteChange() {
    }
}