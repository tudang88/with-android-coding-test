package com.example.myapplication.ui.details

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.common.composables.ImageCardItem
import com.example.myapplication.ui.common.utils.shareImage
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailsScreen(
    id: Int,
    modifier: Modifier = Modifier,
    viewModel: DetailsScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    /**
     * get the user profile
     * by Id
     */
    LaunchedEffect(Unit) {
        viewModel.getUserDetails(id)
    }

    /**
     * share photo
     */
    LaunchedEffect(Unit) {
        viewModel.shareEvent.collectLatest {
            if (it) {
                uiState.value.user?.let { it1 -> shareImage(context, it1.photo) }
                viewModel.clearShareEvent()
            }
        }
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