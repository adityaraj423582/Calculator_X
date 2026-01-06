package com.example.calculatorx.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculatorx.viewmodel.CalculatorAction
import com.example.calculatorx.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    val viewModel = viewModel<CalculatorViewModel>()
    val state = viewModel.state
    var showHistory by remember { mutableStateOf(false) }

    // --- PRO THEME COLORS ---
    val BgTop = Color(0xFF2B2D2F)
    val BgBottom = Color(0xFF1E1E1E)

    val BtnDarkGrey = Color(0xFF333333)    // Numbers (0-9)
    val BtnOrange = Color(0xFFFE9F06)      // Operators (+ - * / =)
    val BtnLightGrey = Color(0xFFA5A5A5)   // Top Row (C +/- %)

    val TextWhite = Color.White
    val TextBlack = Color.Black

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgTop, BgBottom)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // 1. History Icon (Top Left)
            Row(modifier = Modifier.padding(top = 8.dp)) {
                IconButton(onClick = { showHistory = true }) {
                    Icon(Icons.Default.History, contentDescription = "History", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 2. Display Area
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                // History Preview
                Text(
                    text = state.history.lastOrNull()?.split("=")?.firstOrNull() ?: "",
                    fontSize = 28.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 12.dp, bottom = 8.dp)
                )

                // Main Number Display
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.expression.ifEmpty { "0" },
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Light,
                        color = TextWhite,
                        maxLines = 1,
                        lineHeight = 80.sp
                    )

                    // Backspace Icon (Only visible when typing)
                    if (state.expression.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onAction(CalculatorAction.Delete) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Backspace",
                                tint = Color.Gray,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp)) // Keeps alignment stable
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. The Animated Button Grid
            val buttons = listOf(
                "C", "+/-", "%", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "="
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(
                    items = buttons,
                    span = { symbol ->
                        GridItemSpan(if (symbol == "0") 2 else 1)
                    }
                ) { symbol ->
                    val isOrange = symbol in listOf("/", "*", "-", "+", "=")
                    val isLight = symbol in listOf("C", "+/-", "%")
                    val isZero = symbol == "0"

                    val btnColor = when {
                        isOrange -> BtnOrange
                        isLight -> BtnLightGrey
                        else -> BtnDarkGrey
                    }
                    val textColor = if (isLight) TextBlack else TextWhite

                    // Using the Custom Animated Button (Defined below)
                    CalculatorButton(
                        symbol = symbol,
                        backgroundColor = btnColor,
                        textColor = textColor,
                        isWide = isZero,
                        onClick = {
                            val action = when (symbol) {
                                "C" -> CalculatorAction.Clear
                                "=" -> CalculatorAction.Calculate
                                "+/-" -> CalculatorAction.Negate
                                "+", "-", "*", "/", ".", "%" -> CalculatorAction.Symbol(symbol)
                                else -> CalculatorAction.Number(symbol.toIntOrNull() ?: 0)
                            }
                            viewModel.onAction(action)
                        }
                    )
                }
            }
        }

        // 4. History Bottom Sheet
        if (showHistory) {
            ModalBottomSheet(
                onDismissRequest = { showHistory = false },
                containerColor = BgTop
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("History", fontSize = 24.sp, color = TextWhite, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.history.isEmpty()) {
                        Text("No calculations yet.", color = Color.Gray, fontSize = 18.sp)
                    } else {
                        LazyColumn {
                            items(state.history.reversed()) { item ->
                                Text(
                                    text = item,
                                    fontSize = 22.sp,
                                    color = TextWhite,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )
                                HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
                            }
                        }
                    }
                }
            }
        }
    }
}

