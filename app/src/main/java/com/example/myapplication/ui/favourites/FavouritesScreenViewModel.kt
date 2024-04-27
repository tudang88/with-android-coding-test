package com.example.myapplication.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.MyDataSource
import com.example.myapplication.data.User
import com.example.myapplication.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavouritesUiState(
    val items: List<User> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class FavouritesScreenViewModel @Inject constructor(
    private val repository: MyDataSource
) : ViewModel() {
    // loading circle state
    private val _isLoading = MutableStateFlow(false)

    // list of items
    private val _items = repository.getAllFavorites()

    val uiState: StateFlow<FavouritesUiState> = combine(_items, _isLoading) { items, loading ->
        FavouritesUiState(items, loading)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = FavouritesUiState(isLoading = true)
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