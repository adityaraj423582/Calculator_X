package com.example.calculatorx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// --- YOUR SCREENS ---
// Ensure these files exist in your project folders.
// If any are red, delete the import and re-import the correct location.
import com.example.calculatorx.navigation.Screen
import com.example.calculatorx.ui.ConverterScreen
import com.example.calculatorx.ui.FinanceScreen
import com.example.calculatorx.ui.theme.CalculatorScreen
import com.example.calculatorx.ui.theme.CalculatorXTheme
import com.example.calculatorx.ui.theme.ScientificScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorXTheme {
                // UPDATE: Surface ensures the background is always white/dark
                // preventing "glitches" when switching tabs.
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Define the tabs
    val screens = listOf(
        Screen.Calculator,
        Screen.Scientific,
        Screen.Converter,
        Screen.Finance
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // re-selecting the same item
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calculator.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tab 1: Standard Calculator
            composable(Screen.Calculator.route) {
                CalculatorScreen()
            }

            // Tab 2: Scientific Calculator
            composable(Screen.Scientific.route) {
                ScientificScreen()
            }

            // Tab 3: Converter (Length, Weight, etc.)
            composable(Screen.Converter.route) {
                ConverterScreen()
            }

            // Tab 4: Finance (EMI, Loan, etc.)
            composable(Screen.Finance.route) {
                FinanceScreen()
            }
        }
    }
}