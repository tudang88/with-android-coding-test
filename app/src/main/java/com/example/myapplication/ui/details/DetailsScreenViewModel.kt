package com.example.myapplication.ui.details

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

data class DetailsUiState(
    val user: User? = null,
    val isFav: Boolean = false
)

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val repository: MyDataSource
) : ViewModel() {

    private val _isFav = MutableStateFlow(false)
    private val _user = MutableStateFlow<User?>(null)

    val uiState: StateFlow<DetailsUiState> = combine(_user, _isFav) { user, fav ->
        DetailsUiState(user = user, isFav = fav)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        DetailsUiState()
    )

    val shareEvent = repository.observeShareEven()

    /**
     * reset share event
     */
    fun clearShareEvent() {
        repository.emitShareEvent(false)
    }

    /**
     * get User info
     * from data source
     */
    fun getUserDetails(id: Int) {
        viewModelScope.launch {
            val user = repository.getUserById(id)
            _user.value = user
            if (user != null) {
                _isFav.value = user.isFavorite
            }
        }
    }

    /**
     * update favorite
     * mark
     */
    fun onFavouriteChange(value: Boolean) {
        viewModelScope.launch {
            _user.value?.let {
                repository.markFavourite(it.id, value)
                _isFav.value = value
            }
        }
    }
}