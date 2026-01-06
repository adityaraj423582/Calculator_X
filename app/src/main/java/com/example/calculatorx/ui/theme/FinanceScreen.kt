package com.example.calculatorx.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

@Composable
fun FinanceScreen() {
    // --- GOLD & BLACK THEME ---
    val BgColor = Color.Black
    val CardColor = Color(0xFF1E1E1E)
    val GoldAccent = Color(0xFFE6A23C)
    val TextWhite = Color.White
    val TextGray = Color.Gray

    // State for selected tool
    var selectedTool by remember { mutableStateOf("EMI") }
    val tools = listOf("EMI", "GST", "SIP")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(16.dp)
    ) {
        // 1. Header
        Text(
            text = "Finance",
            color = TextWhite,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 2. Tool Selector (Tabs)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            items(tools) { tool ->
                val isSelected = tool == selectedTool
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (isSelected) GoldAccent else CardColor)
                        .clickable { selectedTool = tool }
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = tool,
                        color = if (isSelected) Color.Black else TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 3. The Active Tool UI
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTool) {
                "EMI" -> EMICalculator(CardColor, GoldAccent, TextWhite)
                "GST" -> GSTCalculator(CardColor, GoldAccent, TextWhite)
                "SIP" -> SIPCalculator(CardColor, GoldAccent, TextWhite)
            }
        }
    }
}

// --- TOOLS COMPONENTS ---

@Composable
fun EMICalculator(cardColor: Color, accentColor: Color, textColor: Color) {
    var amount by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FinanceInput(label = "Loan Amount (₹)", value = amount, onValueChange = { amount = it }, cardColor, textColor)
        FinanceInput(label = "Interest Rate (%)", value = rate, onValueChange = { rate = it }, cardColor, textColor)
        FinanceInput(label = "Period (Years)", value = years, onValueChange = { years = it }, cardColor, textColor)

        // Calculate Button
        FinanceButton(text = "Calculate EMI", accentColor = accentColor) {
            val p = amount.toDoubleOrNull() ?: 0.0
            val r = (rate.toDoubleOrNull() ?: 0.0) / 12 / 100
            val n = (years.toDoubleOrNull() ?: 0.0) * 12
            if (p > 0 && r > 0 && n > 0) {
                val emi = (p * r * (1 + r).pow(n)) / ((1 + r).pow(n) - 1)
                result = NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(emi)
            }
        }

        // Result Card
        ResultCard(label = "Monthly EMI", value = result, cardColor, accentColor)
    }
}

@Composable
fun GSTCalculator(cardColor: Color, accentColor: Color, textColor: Color) {
    var amount by remember { mutableStateOf("") }
    var gstRate by remember { mutableStateOf("18") } // Default 18%
    var finalAmount by remember { mutableStateOf("0") }
    var gstAmount by remember { mutableStateOf("0") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FinanceInput(label = "Original Amount (₹)", value = amount, onValueChange = { amount = it }, cardColor, textColor)

        Text("GST Rate: $gstRate%", color = textColor, fontWeight = FontWeight.Bold)
        Slider(
            value = gstRate.toFloat(),
            onValueChange = { gstRate = it.toInt().toString() },
            valueRange = 0f..28f,
            steps = 4, // 5%, 12%, 18%, 28%
            colors = SliderDefaults.colors(thumbColor = accentColor, activeTrackColor = accentColor)
        )

        FinanceButton(text = "Calculate GST", accentColor = accentColor) {
            val p = amount.toDoubleOrNull() ?: 0.0
            val r = gstRate.toDoubleOrNull() ?: 0.0
            val gst = (p * r) / 100
            gstAmount = NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(gst)
            finalAmount = NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(p + gst)
        }

        ResultCard(label = "Total Tax", value = gstAmount, cardColor, accentColor)
        ResultCard(label = "Final Amount", value = finalAmount, cardColor, accentColor)
    }
}

@Composable
fun SIPCalculator(cardColor: Color, accentColor: Color, textColor: Color) {
    var monthly by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var totalValue by remember { mutableStateOf("0") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FinanceInput(label = "Monthly Investment (₹)", value = monthly, onValueChange = { monthly = it }, cardColor, textColor)
        FinanceInput(label = "Expected Return (%)", value = rate, onValueChange = { rate = it }, cardColor, textColor)
        FinanceInput(label = "Time Period (Years)", value = years, onValueChange = { years = it }, cardColor, textColor)

        FinanceButton(text = "Calculate Returns", accentColor = accentColor) {
            val p = monthly.toDoubleOrNull() ?: 0.0
            val i = (rate.toDoubleOrNull() ?: 0.0) / 100 / 12
            val n = (years.toDoubleOrNull() ?: 0.0) * 12
            if (p > 0 && i > 0 && n > 0) {
                val fv = p * ( (1 + i).pow(n) - 1 ) * (1 + i) / i
                totalValue = NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(fv)
            }
        }

        ResultCard(label = "Maturity Value", value = totalValue, cardColor, accentColor)
    }
}

// --- SHARED COMPONENTS ---

@Composable
fun FinanceInput(label: String, value: String, onValueChange: (String) -> Unit, cardColor: Color, textColor: Color) {
    Column {
        Text(text = label, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = cardColor,
                unfocusedContainerColor = cardColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = Color(0xFFE6A23C), // Gold Cursor
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}

@Composable
fun FinanceButton(text: String, accentColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = text, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ResultCard(label: String, value: String, cardColor: Color, accentColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardColor)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.Gray, fontSize = 16.sp)
        Text(text = value, color = accentColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}