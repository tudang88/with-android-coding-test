package com.example.myapplication.ui.master

import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import com.example.myapplication.R
import com.example.myapplication.common.Constants
import com.example.myapplication.ui.navigation.AppNavigationActions
import com.example.myapplication.ui.navigation.Screen

/**
 * configuration for TopAppBar
 */
enum class ActionMenuType {
    Empty,
    ShareMenu
}
enum class TopBarTitle(private val title:String){
    Home("Home"),
    Favourites("Favourites"),
    Details("Details");

    override fun toString() = title
}
data class TopAppBarState(
    val navIcon: ImageVector,
    val title: String,
    val hasActionMenu: ActionMenuType = ActionMenuType.Empty
)

@Composable
fun NavigationIcon(
    navIcon: ImageVector,
    onNavigationIconClick: () -> Unit
) {
    IconButton(onClick = onNavigationIconClick) {
        Icon(
            imageVector = navIcon,
            contentDescription = "Navigation Drawer Button"
        )
    }
}

@Composable
fun CreateActionMenu(
    menuType: ActionMenuType,
    onActionMenuItemClick: (item: String) -> Unit
) {
    when (menuType) {
        ActionMenuType.ShareMenu -> {
            IconButton(
                onClick = {
                    // share result through other app
                    onActionMenuItemClick("Share")
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = "button share"
                )
            }
        }
        else -> {
            // No support for other type of action button
        }
    }
}

/**
 * Top app bar
 * helper function
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun buildTopAppBar(navActions: AppNavigationActions, appBarState: TopAppBarState) {
    val context = LocalContext.current
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text(appBarState.title) },
        navigationIcon = {
            NavigationIcon(navIcon = appBarState.navIcon, onNavigationIconClick = {
                when (appBarState.navIcon) {
                    Icons.AutoMirrored.Filled.ArrowBack -> {
                        // back stack
                        navActions.navigateBack()
                    }

                    else -> {
                        Log.e(Constants.LOG_TAG_COMMON, "Unsupported event on Navigation Button")
                    }
                }
            })
        },
        actions = {
            CreateActionMenu(
                menuType = appBarState.hasActionMenu,
                onActionMenuItemClick = { item ->
                    when (item) {
                        "Share" -> {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "photo url"
                                )
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }
                    }
                }
            )
        },
    )
}

/**
 * Bottom Bar Item
 * data
 */
data class BottomNavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasBadge: Boolean = false
)

/**
 * all bottom bar menu items
 */
val navigationItems = listOf(
    BottomNavigationItem(
        Screen.HomeScreen.route,
        "Home",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        false
    ),
    BottomNavigationItem(
        Screen.FavouriteScreen.route,
        "Favorite",
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder,
        true
    ),
)

/**
 * BottomBar helper function
 */
@Composable
fun buildBottomBar(
    items: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    navActions: AppNavigationActions,
    favBadge: State<Int>
) {
    var selectedIndex = selectedItemIndex
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                label = {
                    Text(text = item.title)
                },
                onClick = {
                    selectedIndex = index
                    // navigate to tab
                    when (item.route) {
                        Screen.HomeScreen.route -> navActions.navigateToHome()
                        Screen.FavouriteScreen.route -> navActions.navigateToFavourites()
                        else -> {
                            Log.e(
                                Constants.LOG_TAG_COMMON,
                                "unknown supported route ${item.route}"
                            )
                        }
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            // only display badge for item needed
                            if (item.hasBadge) {
                                Badge {
                                    Text(text = favBadge.value.toString())
                                }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = if (index == selectedIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon, contentDescription = item.title
                        )
                    }
                },
            )
        }
    }
}
