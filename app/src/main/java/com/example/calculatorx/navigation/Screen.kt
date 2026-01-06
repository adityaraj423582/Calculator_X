package com.example.calculatorx.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Transform
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Calculator : Screen("calculator", "Calc", Icons.Default.Calculate)
    object Scientific : Screen("scientific", "Science", Icons.Default.Science)
    object Converter : Screen("converter", "Convert", Icons.Default.Transform)
    object Finance : Screen("finance", "Finance", Icons.Default.AttachMoney)
}