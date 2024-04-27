package com.example.myapplication.ui.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.MyDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MasterScreenViewModel @Inject constructor(
    private val repository: MyDataSource
) : ViewModel() {
    /**
     * favCount state
     * for updating badge
     */
    val favCount: StateFlow<Int> = repository.getAllFavorites().map {
        it.size
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        0
    )

    /**
     * onShare button
     */
    fun onShareEvent() {
        repository.emitShareEvent(true)
    }
    /**
     * refresh data
     */
    init {
        viewModelScope.launch {
            repository.refresh()
        }
    }
}