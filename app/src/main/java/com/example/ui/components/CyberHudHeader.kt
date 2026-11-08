package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionMode
import com.example.data.model.CompanionProfile
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun CyberHudHeader(
  profile: CompanionProfile,
  isLauraMode: Boolean,
  onSwitchMode: (CompanionMode) -> Unit,
  onOpenSkillGenerator: () -> Unit,
  onOpenHtmlExport: () -> Unit,
  onOpenSettings: () -> Unit,
  isTtsEnabled: Boolean = true,
  onToggleTts: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary

  val infiniteTransition = rememberInfiniteTransition(label = "ping_animation")
  val pingScale by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 1.6f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ping_scale"
  )
  val pingAlpha by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1400, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ping_alpha"
  )

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
    color = Color.Black.copy(alpha = 0.65f)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Logo & Title
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f, fill = false)
        ) {
          // Badge
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(accentColor.copy(alpha = 0.15f))
              .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = profile.badge,
              style = MaterialTheme.typography.titleMedium,
              color = accentColor
            )
            // Pulse ping dot
            Box(
              modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(3.dp)
                .size(7.dp)
                .scale(pingScale)
                .clip(CircleShape)
                .background(CyberEmerald.copy(alpha = pingAlpha))
            )
            Box(
              modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(3.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(CyberEmerald)
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column {
            Text(
              text = profile.osTitle,
              style = MaterialTheme.typography.titleSmall,
              color = accentColor,
              maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .clip(CircleShape)
                  .background(CyberEmerald)
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = "STATUS: ${profile.status} | ${profile.location}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray.copy(alpha = 0.8f),
                fontSize = 10.sp,
                maxLines = 1
              )
            }
          }
        }

        // Action Buttons (Skill Generator, HTML, Settings)
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          if (onToggleTts != null) {
            IconButton(
              onClick = onToggleTts,
              modifier = Modifier
                .size(38.dp)
                .testTag("btn_toggle_tts")
            ) {
              Icon(
                imageVector = if (isTtsEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                contentDescription = if (isTtsEnabled) "Silenciar voz" else "Activar voz",
                tint = if (isTtsEnabled) accentColor else Color.Gray,
                modifier = Modifier.size(20.dp)
              )
            }
          }

          IconButton(
            onClick = onOpenSkillGenerator,
            modifier = Modifier
              .size(38.dp)
              .testTag("btn_skill_generator")
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = "Generador de Skills",
              tint = accentColor,
              modifier = Modifier.size(20.dp)
            )
          }

          IconButton(
            onClick = onOpenHtmlExport,
            modifier = Modifier
              .size(38.dp)
              .testTag("btn_html_export")
          ) {
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = "Exportar HTML",
              tint = Color(0xFF60A5FA),
              modifier = Modifier.size(20.dp)
            )
          }

          IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
              .size(38.dp)
              .testTag("btn_settings")
          ) {
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = "Ajustes",
              tint = Color.LightGray,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.size(8.dp))

      // Persona Switcher Pills (Sandra vs Laura)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(30.dp))
          .background(Color(0xFF0F172A))
          .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(30.dp))
          .padding(3.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        val isSandraSelected = !isLauraMode
        val sandraBg by animateColorAsState(
          targetValue = if (isSandraSelected) GoldPrimary else Color.Transparent,
          label = "sandra_bg"
        )
        val sandraTextColor by animateColorAsState(
          targetValue = if (isSandraSelected) Color(0xFF0A0A0E) else Color.Gray,
          label = "sandra_text"
        )

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(25.dp))
            .background(sandraBg)
            .clickable { onSwitchMode(CompanionMode.SANDRA) }
            .padding(vertical = 7.dp)
            .testTag("btn_sandra_mode"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "SANDRA AI",
            style = MaterialTheme.typography.labelMedium,
            color = sandraTextColor
          )
        }

        val lauraBg by animateColorAsState(
          targetValue = if (isLauraMode) NeonPinkPrimary else Color.Transparent,
          label = "laura_bg"
        )
        val lauraTextColor by animateColorAsState(
          targetValue = if (isLauraMode) Color(0xFF0A0A0E) else Color.Gray,
          label = "laura_text"
        )

        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(25.dp))
            .background(lauraBg)
            .clickable { onSwitchMode(CompanionMode.LAURA) }
            .padding(vertical = 7.dp)
            .testTag("btn_laura_mode"),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "LAURA CYBER",
            style = MaterialTheme.typography.labelMedium,
            color = lauraTextColor
          )
        }
      }
    }
  }
}
