package com.example.calculatorx.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculatorx.viewmodel.CalculatorAction
import com.example.calculatorx.viewmodel.CalculatorViewModel

@Composable
fun ScientificScreen() {
    val viewModel = viewModel<CalculatorViewModel>()
    val state = viewModel.state

    // --- GOLD & BLACK THEME ---
    val bgColor = Color.Black
    val buttonDark = Color(0xFF1E1E1E) // Soft Black for numbers
    val buttonGold = Color(0xFFD4AF37) // Metallic Gold for operators
    val buttonSci = Color(0xFF333333)  // Dark Grey for Sci keys
    val textWhite = Color.White
    val textBlack = Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        // 1. Top Control Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* Open History */ }) {
                Icon(Icons.Default.History, contentDescription = "History", tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 2. Display Area (Big & Clean)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = state.expression.ifEmpty { "0" },
                color = textWhite,
                fontSize = 72.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 72.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. The "Sliding" Scientific Bar
        // This solves the clutter! Swipe left/right to find advanced tools.
        val sciButtons = listOf("sin", "cos", "tan", "ln", "log", "π", "e", "√", "^", "!", "rad")

        Text("Scientific Functions", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            items(sciButtons) { symbol ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(width = 70.dp, height = 50.dp) // Wide, touch-friendly pills
                        .clip(RoundedCornerShape(16.dp))
                        .background(buttonSci)
                        .clickable {
                            viewModel.onAction(CalculatorAction.Symbol(symbol))
                        }
                ) {
                    Text(
                        text = symbol,
                        color = textWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        // 4. The Spacious Number Grid (4 Columns)
        // Back to the layout that looks good.
        val mainButtons = listOf(
            "C", "(", ")", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "⌫", "="
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(mainButtons) { symbol ->
                val isGold = symbol in listOf("÷", "×", "-", "+", "=")
                val isDark = symbol in listOf("C", "(", ")", "⌫")

                // Color Logic
                val btnColor = when {
                    isGold -> buttonGold
                    isDark -> Color.Gray
                    else -> buttonDark
                }

                val textColor = if (isGold || isDark) textBlack else textWhite

                // Reusing the nice animated button
                CalculatorButton(
                    symbol = symbol,
                    backgroundColor = btnColor,
                    textColor = textColor,
                    isWide = false,
                    onClick = {
                        val action = when (symbol) {
                            "C" -> CalculatorAction.Clear
                            "⌫" -> CalculatorAction.Delete
                            "=" -> CalculatorAction.Calculate
                            "÷" -> CalculatorAction.Symbol("/")
                            "×" -> CalculatorAction.Symbol("*")
                            else -> if (symbol.toIntOrNull() != null) CalculatorAction.Number(symbol.toInt()) else CalculatorAction.Symbol(symbol)
                        }
                        viewModel.onAction(action)
                    }
                )
            }
        }
    }
}