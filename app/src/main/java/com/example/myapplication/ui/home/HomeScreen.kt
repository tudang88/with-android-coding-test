package com.example.myapplication.ui.home

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.common.composables.ImageCardItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onItemClicked: (id: Int) -> Unit = {}
) {
    // collect ui state
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val profiles = state.value.items
    LazyVerticalGrid(
        columns = GridCells.Adaptive(250.dp),
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        items(profiles) { profile ->
            ImageCardItem(
                profile,
                modifier = modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
                onFavourite = { favState ->
                    viewModel.onFavouriteChange(profile.id, favState)
                },
                onItemClicked = { id -> onItemClicked(id) }
            )
        }
    }
}