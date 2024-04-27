package com.example.myapplication.ui.navigation

open class Screen(val route: String) {
    object HomeScreen : Screen(route = "${ScreenIds.HomeScreen}")
    object FavouriteScreen : Screen(route = "${ScreenIds.FavouriteScreen}")
    object DetailsScreen : Screen(route = "${ScreenIds.DetailsScreen}")

}

enum class ScreenIds(private val screenId:String) {
    HomeScreen(screenId = "home"),
    FavouriteScreen(screenId = "favourite"),
    DetailsScreen(screenId = "details");

    override fun toString() = screenId
}