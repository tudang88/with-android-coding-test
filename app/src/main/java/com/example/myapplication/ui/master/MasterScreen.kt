package com.example.myapplication.ui.master

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.common.Constants
import com.example.myapplication.ui.navigation.AppNavigationActions
import com.example.myapplication.ui.navigation.AppNavigationGraph
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.theme.MyApplicationTheme


@Composable
fun MasterScreen(
    modifier: Modifier = Modifier,
    viewModel: MasterScreenViewModel = hiltViewModel(),
    items: List<BottomNavigationItem> = navigationItems
) {
    // store top bar state
    var appBarState by remember {
        mutableStateOf(TopAppBarState(Icons.Default.Home, TopBarTitle.Home.toString()))
    }
    // store the selected state
    val selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    // tracking favourite state
    val favBadge = viewModel.favCount.collectAsState()
    // prepare nav host controller
    val navController = rememberNavController()
    val navActions = remember(navController) {
        AppNavigationActions(navController)
    }
    Scaffold(
        topBar = { buildTopAppBar(navActions, appBarState) },
        bottomBar = {
            buildBottomBar(items, selectedItemIndex, navActions, favBadge)
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            MasterScreenContent(it, navController, navActions) { destination ->
                when (destination) {
                    Screen.HomeScreen.route ->
                        appBarState = appBarState.copy(
                            navIcon = Icons.Filled.Home,
                            title = TopBarTitle.Home.toString()
                        )

                    Screen.FavouriteScreen.route ->
                        appBarState = appBarState.copy(
                            navIcon = Icons.Filled.ThumbUp,
                            title = TopBarTitle.Favourites.toString()
                        )

                    else ->
                        appBarState = appBarState.copy(
                            navIcon = Icons.AutoMirrored.Filled.ArrowBack,
                            title = TopBarTitle.Details.toString(),
                            hasActionMenu = ActionMenuType.ShareMenu
                        )

                }
            }
        }
    }
}

/**
 * The place holder for
 * all other children screen
 */
@Composable
fun MasterScreenContent(
    it: PaddingValues,
    navController: NavHostController,
    navActions: AppNavigationActions,
    onNavigate: (dest: String) -> Unit
) {
    AppNavigationGraph(
        navController = navController,
        paddingValues = it,
        navActions = navActions,
        onNavigate = {
            onNavigate(it)
        })
}


@Preview(showBackground = true)
@Composable
fun MasterPreview() {
    MyApplicationTheme {
        MasterScreen()
    }
}