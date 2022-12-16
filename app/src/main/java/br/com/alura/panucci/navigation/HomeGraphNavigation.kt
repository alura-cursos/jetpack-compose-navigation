package br.com.alura.panucci.navigation

import androidx.navigation.*
import br.com.alura.panucci.ui.components.BottomAppBarItem

const val homeGraphRoute = "home"

fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation(startDestination = highlightsRoute, route = homeGraphRoute) {
        highlightsScreen(navController)
        menuScreen(navController)
        drinksScreen(navController)
    }
}

fun NavController.navigateToSingleTopWithPopUpToRoute(
    item: BottomAppBarItem,
) {
    val (route: String, navigate) = when (item) {
        BottomAppBarItem.Drinks -> Pair(
            drinksRoute,
            ::navigateToDrinks
        )
        BottomAppBarItem.Highlight -> Pair(
            highlightsRoute,
            ::navigateToHighlights
        )
        BottomAppBarItem.Menu -> Pair(
            menuRoute,
            ::navigateToMenu
        )
    }
    val navOptions = navOptions {
        launchSingleTop = true
        popUpTo(route)
    }
    navigate(navOptions)
}
