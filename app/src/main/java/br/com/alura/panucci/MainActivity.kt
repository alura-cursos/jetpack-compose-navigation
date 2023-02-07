package br.com.alura.panucci

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.alura.panucci.navigation.AppDestination
import br.com.alura.panucci.navigation.bottomAppBarItems
import br.com.alura.panucci.preferences.dataStore
import br.com.alura.panucci.preferences.userPreferences
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.screens.AuthenticationScreen
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                navController.addOnDestinationChangedListener { _, _, _ ->
                    val routes = navController.backQueue.map {
                        it.destination.route
                    }
                    Log.i("MainActivity", "onCreate: back stack - $routes")
                }
            }
            val backStackEntryState by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntryState?.destination
            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val selectedItem by remember(currentDestination) {
                        val item = currentDestination?.let { destination ->
                            bottomAppBarItems.find {
                                it.destination.route == destination.route
                            }
                        } ?: bottomAppBarItems.first()
                        mutableStateOf(item)
                    }
                    val containsInBottomAppBarItems = currentDestination?.let { destination ->
                        bottomAppBarItems.find {
                            it.destination.route == destination.route
                        }
                    } != null
                    val isShowFab = when (currentDestination?.route) {
                        AppDestination.Menu.route,
                        AppDestination.Drinks.route -> true

                        else -> false
                    }
                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem,
                        onBottomAppBarItemSelectedChange = {
                            val route = it.destination.route
                            navController.navigate(route) {
                                launchSingleTop = true
                                popUpTo(route)
                            }
                        },
                        onFabClick = {
                            navController.navigate(AppDestination.Checkout.route)
                        },
                        isShowTopBar = containsInBottomAppBarItems,
                        isShowBottomBar = containsInBottomAppBarItems,
                        isShowFab = isShowFab,
                        onLogout = {
                            scope.launch {
                                context.dataStore.edit {
                                    it.remove(userPreferences)
                                }
                            }
                            navController.navigate(AppDestination.Authentication.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = AppDestination.Highlight.route
                        ) {
                            composable(AppDestination.Authentication.route) {
                                AuthenticationScreen(
                                    onEnterClick = { user ->
                                        scope.launch {
                                            context.dataStore.edit {
                                                it[userPreferences] = user
                                            }
                                            navController.navigate(AppDestination.Highlight.route) {
                                                popUpTo(navController.graph.id)
                                            }
                                        }
                                    }
                                )
                            }
                            composable(AppDestination.Highlight.route) {
                                var user: String? by remember {
                                    mutableStateOf(null)
                                }
                                var dataState by remember {
                                    mutableStateOf("loading")
                                }
                                LaunchedEffect(null) {
                                    val randomMillis = Random.nextLong(500, 1000)
                                    delay(randomMillis)
                                    user = context.dataStore.data.first()[userPreferences]
                                    dataState = "finished"
                                }
                                Log.i("MainActivity", "onCreate: $user")
                                when (dataState) {
                                    "loading" -> {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Text(
                                                text = "Carregando...",
                                                Modifier
                                                    .fillMaxWidth()
                                                    .align(Alignment.Center),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    "finished" -> {
                                        user?.let {
                                            HighlightsListScreen(
                                                products = sampleProducts,
                                                onNavigateToDetails = { product ->
                                                    navController.navigate(
                                                        "${AppDestination.ProductDetails.route}/${product.id}"
                                                    )
                                                },
                                                onNavigateToCheckout = {
                                                    navController.navigate(AppDestination.Checkout.route)
                                                },
                                            )
                                        } ?: LaunchedEffect(null) {
                                            navController.navigate(AppDestination.Authentication.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            composable(AppDestination.Menu.route) {
                                MenuListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = { product ->
                                        navController.navigate(
                                            "${AppDestination.ProductDetails.route}/${product.id}"
                                        )
                                    },
                                )
                            }
                            composable(AppDestination.Drinks.route) {
                                DrinksListScreen(
                                    products = sampleProducts,
                                    onNavigateToDetails = { product ->
                                        navController.navigate(
                                            "${AppDestination.ProductDetails.route}/${product.id}"
                                        )
                                    },
                                )
                            }
                            composable(
                                "${AppDestination.ProductDetails.route}/{productId}"
                            ) { backStackEntry ->
                                val id = backStackEntry.arguments?.getString("productId")
                                sampleProducts.find {
                                    it.id == id
                                }?.let { product ->
                                    ProductDetailsScreen(
                                        product = product,
                                        onNavigateToCheckout = {
                                            navController.navigate(AppDestination.Checkout.route)
                                        },
                                    )
                                } ?: LaunchedEffect(Unit) {
                                    navController.navigateUp()
                                }
                            }
                            composable(AppDestination.Checkout.route) {
                                CheckoutScreen(
                                    products = sampleProducts,
                                    onPopBackStack = {
                                        navController.navigateUp()
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanucciApp(
    bottomAppBarItemSelected: BottomAppBarItem = bottomAppBarItems.first(),
    onBottomAppBarItemSelectedChange: (BottomAppBarItem) -> Unit = {},
    onFabClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    isShowTopBar: Boolean = false,
    isShowBottomBar: Boolean = false,
    isShowFab: Boolean = false,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            if (isShowTopBar) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Ristorante Panucci")
                    },
                    actions = {
                        IconButton(onClick = onLogout) {
                            Icon(
                                Icons.Filled.ExitToApp,
                                contentDescription = "logout"
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isShowBottomBar) {
                PanucciBottomAppBar(
                    item = bottomAppBarItemSelected,
                    items = bottomAppBarItems,
                    onItemChange = onBottomAppBarItemSelectedChange,
                )
            }
        },
        floatingActionButton = {
            if (isShowFab) {
                FloatingActionButton(
                    onClick = onFabClick
                ) {
                    Icon(
                        Icons.Filled.PointOfSale,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PanucciAppPreview() {
    PanucciTheme {
        Surface {
            PanucciApp {}
        }
    }
}