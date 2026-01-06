package com.example.calculatorx.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConverterScreen() {
    // Theme Colors
    val BgColor = Color.Black
    val CardColor = Color(0xFF1E1E1E)
    val GoldAccent = Color(0xFFE6A23C)
    val TextWhite = Color.White
    val TextGray = Color.Gray

    // State for inputs
    var inputValue by remember { mutableStateOf("1") }
    var outputValue by remember { mutableStateOf("1000") } // Placeholder result
    var selectedCategory by remember { mutableStateOf("Length") }

    val categories = listOf("Length", "Mass", "Area", "Time", "Data")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(16.dp)
    ) {
        // 1. Title
        Text(
            text = "Converter",
            color = TextWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 2. Category Tabs (Horizontal Scroll)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) GoldAccent else CardColor)
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.Black else TextWhite,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // 3. Conversion Cards
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            // "From" Card
            ConverterCard(
                label = "From",
                value = inputValue,
                unit = "Meter",
                onValueChange = { inputValue = it },
                cardColor = CardColor,
                textColor = TextWhite
            )

            // Swap Icon
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { /* Logic to swap units later */ },
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(CardColor)
                        .border(1.dp, GoldAccent, RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Swap", tint = GoldAccent)
                }
            }

            // "To" Card
            ConverterCard(
                label = "To",
                value = outputValue,
                unit = "Centimeter",
                onValueChange = { /* Output is usually read-only */ },
                cardColor = CardColor,
                textColor = GoldAccent // Highlight result
            )
        }

        // 4. Keyboard Placeholder (Optional visual)
        // In a real app, clicking the text field opens the system keyboard
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ConverterCard(
    label: String,
    value: String,
    unit: String,
    onValueChange: (String) -> Unit,
    cardColor: Color,
    textColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(cardColor)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = Color.Gray, fontSize = 14.sp)

            // Unit Selector (Visual only for now)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { /* Open Unit List */ }
            ) {
                Text(text = unit, color = Color.Gray, fontWeight = FontWeight.Bold)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Field
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = textColor,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(textColor),
            modifier = Modifier.fillMaxWidth()
        )
    }
}