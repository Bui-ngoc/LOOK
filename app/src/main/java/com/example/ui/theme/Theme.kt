package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LookDarkColorScheme = darkColorScheme(
  primary = LookGold,
  onPrimary = Color(0xFF1E1400),
  primaryContainer = Color(0xFF3E2D00),
  onPrimaryContainer = LookGoldLight,
  secondary = LookNeonCyan,
  onSecondary = Color(0xFF00363D),
  secondaryContainer = Color(0xFF004F58),
  onSecondaryContainer = Color(0xFF8CF4FF),
  tertiary = LookPurple,
  onTertiary = Color.White,
  background = LookDarkBg,
  onBackground = TextPrimary,
  surface = LookDarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = LookDarkSurfaceVariant,
  onSurfaceVariant = TextSecondary,
  outline = LookBorder,
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = LookDarkColorScheme,
    typography = Typography,
    content = content
  )
}
