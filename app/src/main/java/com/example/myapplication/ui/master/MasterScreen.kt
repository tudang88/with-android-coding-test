package com.example.myapplication.ui.master

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.navigation.AppNavigationActions
import com.example.myapplication.ui.navigation.AppNavigationGraph
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun MasterScreen(
    viewModel: MasterScreenViewModel = hiltViewModel(),
    items: List<BottomNavigationItem> = navigationItems
) {
    // collect ui state from View Model
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val topBarState = uiState.value.appBarState
    // store the selected state cross configuration change
    val selectedItemIndex = uiState.value.bottomTabIndex
    // tracking favourite state
    val favBadge = uiState.value.favCount
    // prepare nav host controller
    val navController = rememberNavController()
    val navActions = remember(navController) {
        AppNavigationActions(navController)
    }
    // bottom bar state
    val bottomBarState = topBarState.visibleBottomBar

    Scaffold(
        topBar = {
            buildTopAppBar(navActions, topBarState) {
                // propagate action button event
                viewModel.onShareEvent()
            }
        },
        bottomBar = {
            if (bottomBarState)
                buildBottomBar(items, selectedItemIndex, navActions, favBadge) { selectedIdx ->
                    viewModel.onTabSelected(selectedIdx)
                }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            MasterScreenContent(navController, navActions) { dest ->
                viewModel.onScreenTransition(dest)
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
    navController: NavHostController,
    navActions: AppNavigationActions,
    onNavigate: (dest: String) -> Unit
) {
    AppNavigationGraph(
        navController = navController,
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