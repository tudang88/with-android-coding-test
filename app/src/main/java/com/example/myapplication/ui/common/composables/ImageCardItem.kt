package com.example.myapplication.ui.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.data.User

/**
 * This common item will be used in
 * HomeScreen and FavouritesScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCardItem(
    user: User,
    modifier: Modifier = Modifier,
    color: Color = Color(0xffE91E63),
    enableClick: Boolean = true,
    onItemClicked: (id: Int) -> Unit = {},
    onFavourite: (value: Boolean) -> Unit = {}
) {
    /**
     * favourite status
     */
    var isFavourite by remember {
        mutableStateOf(user.isFavorite)
    }
    Box {
        Card(
            modifier = modifier,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).data(user.photo)
                    .build(),
                contentDescription = stringResource(
                    id = R.string.user_photo
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enableClick,
                        onClick =
                        { onItemClicked(user.id) }
                    ),
                //placeholder =
                //, error =
                //contentScale =
            )
        }

        IconToggleButton(
            checked = isFavourite,
            modifier = Modifier.align(Alignment.TopEnd),
            onCheckedChange = {
                isFavourite = !isFavourite
                // notify event
                onFavourite(isFavourite)
            },
        ) {
            Icon(
                tint = color, modifier = modifier.graphicsLayer {
                    scaleX = 1.3f
                    scaleY = 1.3f
                },
                imageVector = if (isFavourite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = null
            )
        }
    }
}