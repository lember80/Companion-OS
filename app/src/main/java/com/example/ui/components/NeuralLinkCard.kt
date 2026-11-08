package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.CompanionProfile
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun NeuralLinkCard(
  profile: CompanionProfile,
  isLauraMode: Boolean,
  isSyncActive: Boolean,
  onSyncToggle: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary
  val cardBg = if (isLauraMode) Color(0xFF180A1A).copy(alpha = 0.85f) else Color(0xFF14120C).copy(alpha = 0.85f)

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
    color = cardBg
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Sensors,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = profile.connTitle,
            style = MaterialTheme.typography.labelMedium,
            color = accentColor
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSyncActive) CyberEmerald.copy(alpha = 0.2f) else Color.DarkGray.copy(alpha = 0.3f))
            .border(
              1.dp,
              if (isSyncActive) CyberEmerald.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.3f),
              RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Text(
            text = if (isSyncActive) "ACTIVO" else "STANDBY",
            style = MaterialTheme.typography.labelSmall,
            color = if (isSyncActive) CyberEmerald else Color.Gray
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Metric & Switch
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (isSyncActive) profile.connMetric else "--.-%",
            style = MaterialTheme.typography.displayMedium,
            color = if (isSyncActive) Color.White else Color.Gray
          )
          Text(
            text = "Sincronización Neuronal (${profile.userName})",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray.copy(alpha = 0.7f)
          )
        }

        Switch(
          checked = isSyncActive,
          onCheckedChange = onSyncToggle,
          colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = accentColor,
            uncheckedThumbColor = Color.Gray,
            uncheckedTrackColor = Color.DarkGray
          ),
          modifier = Modifier.testTag("switch_neural_sync")
        )
      }
    }
  }
}
