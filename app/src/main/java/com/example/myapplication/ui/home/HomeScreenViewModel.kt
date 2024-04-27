package com.example.myapplication.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.MyDataSource
import com.example.myapplication.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val items: List<User> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: MyDataSource
): ViewModel() {
    // loading circle state
    private val _isLoading = MutableStateFlow(false)
    // list of items
    private val _items = repository.getAllCurrentItems()

    val uiState: StateFlow<HomeUiState> = combine(_items, _isLoading) {
        items, loading ->
        HomeUiState(items, loading)
    }.stateIn(scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState(isLoading = true)
    )

    /**
     * update favorite
     * mark
     */
    fun onFavouriteChange(id: Int, value: Boolean) {
        viewModelScope.launch {
            repository.markFavourite(id, value)
        }
    }
}