package com.example.myapplication.ui.master

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.navigation.AppNavigationGraph
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Bottom Bar Item
 * data
 */
data class BottomNavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
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
        false,
        0
    ),
)

@Composable
fun MasterScreen(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItem> = navigationItems
) {
    // store the selected state
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        label = {
                            Text(text = item.title)
                        },
                        onClick = {
                            selectedItemIndex = index
                             navController.navigate(item.route)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    // only display badge for item needed
                                    if (item.badgeCount != null && item.badgeCount != 0) {
                                        Badge {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    }
                                },
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon, contentDescription = item.title
                                )
                            }
                        },
                    )
                }
            }
        }
    ) {
        MasterScreenContent(it, navController)
    }
}

/**
 * The place holder for
 * all other children screen
 */
@Composable
fun MasterScreenContent(it: PaddingValues, navController: NavHostController) {
    AppNavigationGraph(navController = navController, paddingValues = it, onNavigate = {})
}


@Preview(showBackground = true)
@Composable
fun MasterPreview() {
    MyApplicationTheme {
        MasterScreen()
    }
}