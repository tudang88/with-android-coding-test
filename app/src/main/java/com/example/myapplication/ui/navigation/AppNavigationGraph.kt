package com.example.myapplication.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.ui.details.DetailsScreen
import com.example.myapplication.ui.favourites.FavouritesScreen
import com.example.myapplication.ui.home.HomeScreen
import com.example.myapplication.ui.navigation.AppNavigationArgs.USER_ID

@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    navActions: AppNavigationActions,
    onNavigate: (dest: String) -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        /**
         * Home Route
         */
        composable(route = Screen.HomeScreen.route) {
            onNavigate(Screen.HomeScreen.route)
            HomeScreen { clickedId ->
                navActions.navigateToDetails(clickedId)
            }
        }
        /**
         * Favourite Route
         */
        composable(route = Screen.FavouriteScreen.route) {
            onNavigate(Screen.FavouriteScreen.route)
            FavouritesScreen { clickedId ->
                navActions.navigateToDetails(clickedId)
            }
        }
        /**
         * Details Route
         * only details screen has animation
         */
        composable(
            route = Screen.DetailsScreen.route + "/{$USER_ID}",
            arguments = listOf(navArgument(USER_ID) {
                type = NavType.IntType
                defaultValue = 0
            }),
//            enterTransition = {
//                slideInHorizontally(
//                    initialOffsetX = { it }, // it == fullWidth
//                    animationSpec = tween(
//                        durationMillis = 200,
//                        easing = LinearEasing
//                    )
//                )
//            },
//            exitTransition = {
//                slideOutHorizontally(
//                    targetOffsetX = { it },
//                    animationSpec = tween(
//                        durationMillis = 200,
//                        easing = LinearEasing
//                    )
//                )
//            },
        ) { entry ->
            onNavigate(Screen.DetailsScreen.route)
            entry.arguments?.getInt(USER_ID)?.let { DetailsScreen(it) }

        }
    }
}