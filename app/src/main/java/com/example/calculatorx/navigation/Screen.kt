package com.example.calculatorx.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Calculator : Screen("calculator", "Calculator", Icons.Default.Calculate)
    object Scientific : Screen("scientific", "Scientific", Icons.Default.Functions)
    object Converter : Screen("converter", "Converter", Icons.Default.Straighten)
    object Finance : Screen("finance", "Finance", Icons.Default.Money)
}