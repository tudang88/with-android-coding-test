package com.example.myapplication.ui.details

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.common.composables.ImageCardItem

@Composable
fun DetailsScreen(
    id: Int,
    modifier: Modifier = Modifier,
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    /**
     * get the user profile
     * by Id
     */
    LaunchedEffect(Unit) {
        viewModel.getUserDetails(id)
    }

    /**
     * setup ui
     */
    uiState.value.user?.let {
        ImageCardItem(
            it,
            enableClick = false,
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth()
                .aspectRatio(1.5f),
            onFavourite = { favState ->
                viewModel.onFavouriteChange(favState)
            }
        )
    }
}