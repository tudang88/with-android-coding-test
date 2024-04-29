package com.example.myapplication.ui.details

import com.example.myapplication.MainCoroutineRule
import com.example.myapplication.data.FakeDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsScreenViewModelTest {
    // test target
    private lateinit var detailsScreenViewModel: DetailsScreenViewModel    // use a fake repository to be injected into the viewmodel
    private lateinit var dataRepository: FakeDataRepository

    // set the main coroutines dispatcher for unit testing
    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        // init repository
        dataRepository = FakeDataRepository()
        detailsScreenViewModel = DetailsScreenViewModel(dataRepository)
    }

    @Test
    fun getShareEvent() {
    }

    @Test
    fun clearShareEvent() {
    }

    @Test
    fun getUserDetails() {
    }

    @Test
    fun onFavouriteChange() {
    }
}