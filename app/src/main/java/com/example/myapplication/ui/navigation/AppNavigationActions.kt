package com.example.myapplication.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object AppNavigationArgs {
    const val USER_ID = "id"
}

/**
 * Models the navigation actions in the app.
 */
class AppNavigationActions(private val navController: NavHostController) {
    fun navigateToDetails(id: Int) {
        navController.navigate(Screen.DetailsScreen.buildRouteWithIntArgs(id))
    }

    fun navigateToHome() {
        navController.navigate(Screen.HomeScreen.route) {
            // popup to current tab screen to prevent inconsistent back stack
            popUpTo(Screen.HomeScreen.route)
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }
    }

    fun navigateToFavourites() {
        navController.navigate(Screen.FavouriteScreen.route) {
            // popup to current tab screen to prevent inconsistent back stack
            popUpTo(Screen.FavouriteScreen.route)
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}