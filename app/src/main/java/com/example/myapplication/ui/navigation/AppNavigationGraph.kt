package com.example.myapplication.ui.navigation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.details.DetailsScreen
import com.example.myapplication.ui.favourites.FavouritesScreen
import com.example.myapplication.ui.home.HomeScreen

@Composable
fun AppNavigationGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onNavigate: (screenId: String) -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        /**
         * Home Route
         */
        composable(route = Screen.HomeScreen.route) {
            HomeScreen()
        }
        /**
         * Favourite Route
         */
        composable(route = Screen.FavouriteScreen.route) {
            FavouritesScreen()
        }
        /**
         * Details Route
         * only details screen has animation
         */
        composable(
            route = Screen.DetailsScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it }, // it == fullWidth
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                )
            },
        ) {
            DetailsScreen()
        }
    }
}