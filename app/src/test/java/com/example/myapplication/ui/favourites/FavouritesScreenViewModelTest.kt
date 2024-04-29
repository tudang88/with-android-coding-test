package com.example.myapplication.ui.favourites

import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.data.FakeDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun onFavouriteChange() {
    }
}