package com.example.myapplication.ui.favourites

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.common.composables.ImageCardItem

@Composable
fun FavouritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavouritesScreenViewModel = hiltViewModel(),
    onItemClicked: (id: Int) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsState()
    val profiles = state.value.items
    LazyVerticalGrid(
        columns = GridCells.Adaptive(250.dp),
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        items(
            profiles,
            key = {
                it.id
            }
        ) { profile ->

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