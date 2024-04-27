package com.example.myapplication.ui.navigation

open class Screen(val route: String) {
    object HomeScreen : Screen(route = "${ScreenIds.HomeScreen}")
    object FavouriteScreen : Screen(route = "${ScreenIds.FavouriteScreen}")
    object DetailsScreen : Screen(route = "${ScreenIds.DetailsScreen}")

    fun buildRouteWithIntArgs(vararg args: Int): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}

enum class ScreenIds(private val screenId:String) {
    HomeScreen(screenId = "home"),
    FavouriteScreen(screenId = "favourite"),
    DetailsScreen(screenId = "details");

    override fun toString() = screenId
}