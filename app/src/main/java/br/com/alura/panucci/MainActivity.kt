package br.com.alura.panucci

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.alura.panucci.sampledata.bottomAppBarItems
import br.com.alura.panucci.sampledata.sampleProductWithImage
import br.com.alura.panucci.sampledata.sampleProducts
import br.com.alura.panucci.ui.components.BottomAppBarItem
import br.com.alura.panucci.ui.components.PanucciBottomAppBar
import br.com.alura.panucci.ui.screens.*
import br.com.alura.panucci.ui.theme.PanucciTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val initialScreen = "Destaques"
            val screens = remember {
                mutableStateListOf(initialScreen)
            }
            Log.i("MainActivity", "onCreate: screens ${screens.toList()}")
            val currentScreen = screens.last()
            BackHandler(screens.size > 1) {
                screens.removeLast()
            }
            PanucciTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedItem by remember(currentScreen) {
                        val item = bottomAppBarItems.find { currentScreen == it.label }
                        mutableStateOf(item)
                    }
                    PanucciApp(
                        bottomAppBarItemSelected = selectedItem ?: bottomAppBarItems.first(),
                        onBottomAppBarItemSelectedChange = {
                            selectedItem = it
                            screens.add(it.label)
                        },
                        onFabClick = {
                            screens.add("Pedido")
                        }) {
                        when (currentScreen) {
                            "Destaques" -> HighlightsListScreen(
                                products = sampleProducts,
                                onOrderClick = {
                                    screens.add("Pedido")
                                },
                                onProductClick = {
                                    screens.add("DetalhesProduto")
                                }
                            )
                            "Menu" -> MenuListScreen(
                                products = sampleProducts
                            )
                            "Bebidas" -> DrinksListScreen(
                                products = sampleProducts + sampleProducts
                            )
                            "DetalhesProduto" -> ProductDetailsScreen(
                                product = sampleProductWithImage
                            )
                            "Pedido" -> CheckoutScreen(products = sampleProducts)
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
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Ristorante Panucci")
                },
            )
        },
        bottomBar = {
            PanucciBottomAppBar(
                item = bottomAppBarItemSelected,
                items = bottomAppBarItems,
                onItemChange = onBottomAppBarItemSelectedChange,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick
            ) {
                Icon(
                    Icons.Filled.PointOfSale,
                    contentDescription = null
                )
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