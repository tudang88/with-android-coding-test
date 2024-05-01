package com.example.myapplication.ui.master

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ThumbUp
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import com.example.myapplication.R
import com.example.myapplication.common.Constants
import com.example.myapplication.ui.navigation.AppNavigationActions
import com.example.myapplication.ui.navigation.Screen
import timber.log.Timber

/**
 * configuration for TopAppBar
 */
enum class ActionMenuType {
    Empty,
    ShareMenu
}

enum class TopBarTitle(private val title: String) {
    Home("Home"),
    Favourites("Favourites"),
    Details("Details");

    override fun toString() = title
}

data class AppBarAndBottomBarState(
    val navIcon: ImageVector,
    val title: String,
    val hasActionMenu: ActionMenuType = ActionMenuType.Empty,
    val visibleBottomBar: Boolean = true,
    val enableClick: Boolean = false
)

/**
 * Helper to determine
 * Top Bar And Bottom Bar
 * state
 */
fun AppBarAndBottomBarState.screenTransition(dest: String): AppBarAndBottomBarState {
    val destinationMap = mapOf(
        Screen.HomeScreen.route to AppBarAndBottomBarState(
            Icons.Filled.Home,
            TopBarTitle.Home.toString(),
            ActionMenuType.Empty
        ),
        Screen.FavouriteScreen.route to AppBarAndBottomBarState(
            Icons.Filled.ThumbUp,
            TopBarTitle.Favourites.toString(),
            ActionMenuType.Empty
        ),
    )

    return destinationMap[dest]
        ?: AppBarAndBottomBarState(
            Icons.Default.KeyboardArrowLeft,
            TopBarTitle.Details.toString(),
            ActionMenuType.ShareMenu,
            false,
            true
        )
}

@Composable
fun NavigationIcon(
    navIcon: ImageVector,
    enableClick: Boolean = false,
    onNavigationIconClick: () -> Unit
) {
    IconButton(enabled = enableClick, onClick = onNavigationIconClick) {
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
fun BuildTopAppBar(
    navActions: AppNavigationActions,
    appBarState: AppBarAndBottomBarState,
    onShareButtonClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text(appBarState.title) },
        navigationIcon = {
            NavigationIcon(
                navIcon = appBarState.navIcon,
                enableClick = appBarState.enableClick,
                onNavigationIconClick = {
                    when (appBarState.navIcon) {
                        Icons.Default.KeyboardArrowLeft -> {
                            // back stack
                            navActions.navigateBack()
                        }

                        else -> {
                            Timber.tag(Constants.LOG_TAG_COMMON)
                                .e("Unsupported event on Navigation Button")
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
                            // propagate event
                            onShareButtonClicked()
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
    val stringResId: Int,
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
        R.string.home_tab,
        Icons.Filled.Home,
        Icons.Outlined.Home,
        false
    ),
    BottomNavigationItem(
        Screen.FavouriteScreen.route,
        R.string.fav_tab,
        Icons.Filled.Favorite,
        Icons.Outlined.FavoriteBorder,
        true
    ),
)

/**
 * BottomBar helper function
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun buildBottomBar(
    items: List<BottomNavigationItem>,
    selectedIndex: Int,
    navActions: AppNavigationActions,
    favBadge: Int,
    onSelectedIndex: (Int) -> Unit
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                label = {
                    Text(text = stringResource(id = item.stringResId))
                },
                onClick = {
                    onSelectedIndex(index)
                    // navigate to tab
                    when (item.route) {
                        Screen.HomeScreen.route -> navActions.navigateToHome()
                        Screen.FavouriteScreen.route -> navActions.navigateToFavourites()
                        else -> {
                            Timber.tag(Constants.LOG_TAG_COMMON)
                                .e("unknown supported route %s", item.route)
                        }
                    }
                },
                icon = {
                    BadgedBox(
                        badge = {
                            // only display badge for item needed
                            if (item.hasBadge) {
                                Badge {
                                    Text(text = favBadge.toString())
                                }
                            }
                        },
                    ) {
                        Icon(
                            imageVector = if (index == selectedIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = stringResource(id = item.stringResId)
                        )
                    }
                },
            )
        }
    }
}

