package com.pawknits.demo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Pumpkin = Color(0xFFE8743B)
val Cocoa = Color(0xFF5B3A29)
val Cream = Color(0xFFFFF8F1)
val Oat = Color(0xFFF3E6D8)
val Berry = Color(0xFFD63A3A)
val Moss = Color(0xFF2E7D4F)

private val PawKnitsColors = lightColorScheme(
    primary = Pumpkin,
    onPrimary = Color.White,
    primaryContainer = Oat,
    onPrimaryContainer = Cocoa,
    secondary = Cocoa,
    onSecondary = Color.White,
    background = Cream,
    onBackground = Cocoa,
    surface = Cream,
    onSurface = Cocoa,
    surfaceVariant = Oat,
    onSurfaceVariant = Cocoa.copy(alpha = 0.75f),
    error = Berry,
)

@Composable
fun PawKnitsTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PawKnitsColors, content = content)
}
