package com.example.calculatorx.ui.theme

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalculatorButton(
    symbol: String,
    backgroundColor: Color,
    textColor: Color,
    isWide: Boolean = false,
    onClick: () -> Unit
) {
    // 1. Get the Android View (needed for the "Pro" vibration)
    val view = LocalView.current

    // 2. Animation State
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale Animation (Shrink to 90%)
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.90f else 1f, label = "scale")

    Box(
        contentAlignment = if (isWide) Alignment.CenterStart else Alignment.Center,
        modifier = Modifier
            .aspectRatio(if (isWide) 2f else 1f)
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // No ripple for cleaner look
                onClick = {
                    // FIX IS HERE: Use CLOCK_TICK for a crisp, high-end click
                    view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                    onClick()
                }
            )
    ) {
        Text(
            text = symbol,
            fontSize = 36.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            // Left padding for the wide "0"
            modifier = Modifier.padding(start = if (isWide) 34.dp else 0.dp)
        )
    }
}