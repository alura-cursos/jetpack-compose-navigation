package br.com.alura.panucci.navigation

import br.com.alura.panucci.ui.components.BottomAppBarItem

sealed class AppDestination(val route: String) {
    object Highlight : AppDestination("highlight")
    object Menu : AppDestination("menu")
    object Drinks : AppDestination("drinks")
    object ProductDetails : AppDestination("productDetails")
    object Checkout : AppDestination("checkout")
}

val bottomAppBarItems = listOf<BottomAppBarItem>(
    BottomAppBarItem.Highlight,
    BottomAppBarItem.Menu,
    BottomAppBarItem.Drinks
)