package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CompanionProfile
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun AvatarCenterpiece(
  profile: CompanionProfile,
  isLauraMode: Boolean,
  isVoiceActive: Boolean,
  onToggleVoice: () -> Unit,
  onQuickPromptSelected: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary
  val cardBg = if (isLauraMode) Color(0xFF180A1A).copy(alpha = 0.85f) else Color(0xFF14120C).copy(alpha = 0.85f)
  var showFullScreenPreview by remember { mutableStateOf(false) }

  val voiceTransition = rememberInfiniteTransition(label = "voice_mic_pulse")
  val micScale by voiceTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (isVoiceActive) 1.25f else 1f,
    animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "mic_scale"
  )

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.5.dp, accentColor.copy(alpha = 0.45f), RoundedCornerShape(16.dp)),
    color = cardBg
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = 0.6f))
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = profile.roleTitle,
            style = MaterialTheme.typography.labelSmall,
            color = accentColor,
            fontSize = 11.sp
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(CyberEmerald.copy(alpha = 0.2f))
            .border(1.dp, CyberEmerald.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = "LIVE LINK",
            style = MaterialTheme.typography.labelSmall,
            color = CyberEmerald,
            fontSize = 9.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Main Avatar Portrait
      Box(
        modifier = Modifier
          .fillMaxWidth(0.92f)
          .aspectRatio(9f / 13f)
          .clip(RoundedCornerShape(14.dp))
          .border(1.5.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
          .clickable { showFullScreenPreview = true },
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = profile.avatarDrawable),
          contentDescription = profile.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier.matchParentSize()
        )

        // Holographic corner lines overlay
        Box(
          modifier = Modifier
            .matchParentSize()
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
        )

        // Fullscreen indicator pill
        Box(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.Black.copy(alpha = 0.7f))
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Fullscreen,
              contentDescription = "Ver completo",
              tint = accentColor,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "HD",
              style = MaterialTheme.typography.labelSmall,
              color = accentColor,
              fontSize = 9.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Voice Status Box
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(Color.Black.copy(alpha = 0.7f))
          .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          IconButton(
            onClick = onToggleVoice,
            modifier = Modifier
              .scale(micScale)
              .size(36.dp)
              .clip(CircleShape)
              .background(accentColor)
              .testTag("btn_voice_mic")
          ) {
            Icon(
              imageVector = Icons.Default.Mic,
              contentDescription = "Micrófono",
              tint = Color(0xFF0F172A),
              modifier = Modifier.size(18.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Column {
            Text(
              text = if (isVoiceActive) "Escuchando voz..." else "Escucha de Voz Activa",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White
            )
            Text(
              text = if (isVoiceActive) "Habla ahora en japonés o español" else "Presiona para hablar en japonés",
              style = MaterialTheme.typography.bodySmall,
              fontSize = 10.sp,
              color = Color.LightGray
            )
          }
        }

        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (isVoiceActive) Color.Red else accentColor)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Quick Phrase Chips (grid of buttons)
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        profile.quickPrompts.take(3).forEach { prompt ->
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Color.Black.copy(alpha = 0.45f))
              .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
              .clickable { onQuickPromptSelected(prompt) }
              .padding(horizontal = 8.dp, vertical = 6.dp)
          ) {
            Text(
              text = prompt,
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFFCBD5E1),
              fontSize = 11.sp,
              maxLines = 1
            )
          }
        }
      }
    }
  }

  // Full-Screen Image Dialog
  if (showFullScreenPreview) {
    Dialog(onDismissRequest = { showFullScreenPreview = false }) {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(2.dp, accentColor, RoundedCornerShape(20.dp)),
        color = Color.Black
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
        ) {
          Image(
            painter = painterResource(id = profile.avatarDrawable),
            contentDescription = profile.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
          )

          IconButton(
            onClick = { showFullScreenPreview = false },
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(12.dp)
              .size(36.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.7f))
              .border(1.dp, accentColor, CircleShape)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Cerrar",
              tint = accentColor,
              modifier = Modifier.size(20.dp)
            )
          }

          Text(
            text = "${profile.name} (Modelo Oficial)",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .padding(bottom = 14.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(Color.Black.copy(alpha = 0.75f))
              .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
              .padding(horizontal = 12.dp, vertical = 4.dp)
          )
        }
      }
    }
  }
}
