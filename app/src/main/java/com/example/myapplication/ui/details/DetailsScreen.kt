package com.example.myapplication.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Column {
            ImageCardItem(
                it,
                enableClick = false,
                modifier = Modifier
                    .padding(4.dp),
                onFavourite = { favState ->
                    viewModel.onFavouriteChange(favState)
                }
            )
            Text(
                text = it.nickname, style = TextStyle(
                    fontSize = 24.sp,
                    shadow = Shadow(
                        color = Color.Blue, offset = Offset(5.0f, 10.0f), blurRadius = 3f
                    ),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}