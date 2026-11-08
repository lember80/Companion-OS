package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary
import com.example.ui.viewmodel.DashboardTab

@Composable
fun HolographicBottomNav(
  currentTab: DashboardTab,
  isLauraMode: Boolean,
  onTabSelected: (DashboardTab) -> Unit,
  onOpenSettings: () -> Unit,
  modifier: Modifier = Modifier
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding()
      .padding(horizontal = 14.dp, vertical = 6.dp)
      .clip(RoundedCornerShape(24.dp))
      .border(1.5.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
    color = Color.Black.copy(alpha = 0.78f)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 6.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavTabItem(
        icon = Icons.Default.Home,
        label = "HUD",
        selected = currentTab == DashboardTab.HUD,
        accentColor = accentColor,
        testTag = "nav_tab_hud",
        onClick = { onTabSelected(DashboardTab.HUD) }
      )

      NavTabItem(
        icon = Icons.Default.ChatBubbleOutline,
        label = "Chat AI",
        selected = currentTab == DashboardTab.CHAT,
        accentColor = accentColor,
        testTag = "nav_tab_chat",
        onClick = { onTabSelected(DashboardTab.CHAT) }
      )

      NavTabItem(
        icon = Icons.AutoMirrored.Filled.MenuBook,
        label = "Kanjis",
        selected = currentTab == DashboardTab.KANJI,
        accentColor = accentColor,
        testTag = "nav_tab_kanji",
        onClick = { onTabSelected(DashboardTab.KANJI) }
      )

      NavTabItem(
        icon = Icons.Default.AutoAwesome,
        label = "Skills",
        selected = currentTab == DashboardTab.SKILLS,
        accentColor = accentColor,
        testTag = "nav_tab_skills",
        onClick = { onTabSelected(DashboardTab.SKILLS) }
      )

      NavTabItem(
        icon = Icons.Default.Settings,
        label = "Ajustes",
        selected = false,
        accentColor = accentColor,
        testTag = "nav_tab_settings",
        onClick = onOpenSettings
      )
    }
  }
}

@Composable
private fun NavTabItem(
  icon: ImageVector,
  label: String,
  selected: Boolean,
  accentColor: Color,
  testTag: String,
  onClick: () -> Unit
) {
  val iconColor by animateColorAsState(
    targetValue = if (selected) accentColor else Color.Gray,
    label = "tab_icon_color"
  )
  val bgColor by animateColorAsState(
    targetValue = if (selected) accentColor.copy(alpha = 0.16f) else Color.Transparent,
    label = "tab_bg_color"
  )

  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(16.dp))
      .background(bgColor)
      .clickable { onClick() }
      .padding(horizontal = 12.dp, vertical = 6.dp)
      .testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = iconColor,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontSize = 10.sp,
        color = iconColor
      )
    }
  }
}
