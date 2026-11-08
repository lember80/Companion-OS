package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val SandraDarkColorScheme = darkColorScheme(
  primary = GoldPrimary,
  onPrimary = Color(0xFF0F172A),
  primaryContainer = GoldContainer,
  onPrimaryContainer = GoldSecondary,
  secondary = GoldSecondary,
  onSecondary = Color(0xFF0F172A),
  tertiary = CyberEmerald,
  background = GoldBackground,
  onBackground = CyberTextWhite,
  surface = GoldSurface,
  onSurface = CyberTextWhite,
  surfaceVariant = Color(0xFF1E1A12),
  onSurfaceVariant = CyberTextMuted,
  outline = GoldBorder
)

val LauraDarkColorScheme = darkColorScheme(
  primary = NeonPinkPrimary,
  onPrimary = Color(0xFF0F172A),
  primaryContainer = NeonContainer,
  onPrimaryContainer = NeonPinkSecondary,
  secondary = NeonPinkSecondary,
  onSecondary = Color(0xFF0F172A),
  tertiary = CyberPurple,
  background = NeonBackground,
  onBackground = CyberTextWhite,
  surface = NeonSurface,
  onSurface = CyberTextWhite,
  surfaceVariant = Color(0xFF261226),
  onSurfaceVariant = CyberTextMuted,
  outline = NeonBorder
)

@Composable
fun CompanionOSTheme(
  isLauraMode: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = if (isLauraMode) LauraDarkColorScheme else SandraDarkColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = CompanionTypography,
    content = content
  )
}
