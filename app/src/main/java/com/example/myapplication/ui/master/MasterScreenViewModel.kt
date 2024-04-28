package com.example.myapplication.ui.master

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.MyDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * master screen Ui
 * state holder class
 */
data class MasterScreenUiState(
    val appBarState: AppBarAndBottomBarState = AppBarAndBottomBarState(
        Icons.Filled.Home,
        TopBarTitle.Home.toString(),
    ),
    val favCount: Int = 0,
    val bottomTabIndex: Int = 0
)

@HiltViewModel
class MasterScreenViewModel @Inject constructor(
    private val repository: MyDataSource
) : ViewModel() {
    // top appbar state
    private val _topBarState =
        MutableStateFlow(AppBarAndBottomBarState(Icons.Filled.Home, TopBarTitle.Home.toString()))

    // favourite badge state
    private val _favCount: StateFlow<Int> = repository.getAllFavorites().map {
        it.size
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        0
    )

    // bottom tab index state
    private val _bottomTabIndex = MutableStateFlow(0)

    // whole screen state
    val uiState: StateFlow<MasterScreenUiState> =
        combine(_topBarState, _favCount, _bottomTabIndex) { t, f, b ->
            MasterScreenUiState(t, f, b)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            MasterScreenUiState()
        )

    /**
     * onShare button
     */
    fun onShareEvent() {
        repository.emitShareEvent(true)
    }

    /**
     * change TopAppBar
     */
    fun onScreenTransition(dest: String) {
        _topBarState.value = _topBarState.value.screenTransition(dest)
    }

    /**
     * update bottom tab index state
     */
    fun onTabSelected(index: Int) {
        _bottomTabIndex.value = index
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