package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionProfile
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun MoodStatusCard(
  profile: CompanionProfile,
  isLauraMode: Boolean,
  modifier: Modifier = Modifier
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary
  val cardBg = if (isLauraMode) Color(0xFF180A1A).copy(alpha = 0.85f) else Color(0xFF14120C).copy(alpha = 0.85f)

  val infiniteTransition = rememberInfiniteTransition(label = "cat_soundwave")
  val bar1Height by infiniteTransition.animateFloat(
    initialValue = 4f,
    targetValue = 18f,
    animationSpec = infiniteRepeatable(tween(550, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b1"
  )
  val bar2Height by infiniteTransition.animateFloat(
    initialValue = 16f,
    targetValue = 6f,
    animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b2"
  )
  val bar3Height by infiniteTransition.animateFloat(
    initialValue = 8f,
    targetValue = 20f,
    animationSpec = infiniteRepeatable(tween(480, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b3"
  )
  val bar4Height by infiniteTransition.animateFloat(
    initialValue = 14f,
    targetValue = 4f,
    animationSpec = infiniteRepeatable(tween(620, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b4"
  )

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
        Text(
          text = profile.moodTitle,
          style = MaterialTheme.typography.labelMedium,
          color = accentColor
        )
        Text(
          text = profile.moodBadge,
          style = MaterialTheme.typography.labelSmall,
          color = accentColor
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Progress info
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = profile.labelMood,
          style = MaterialTheme.typography.bodySmall,
          color = Color.LightGray
        )
        Text(
          text = "${(profile.moodPercent * 100).toInt()}%",
          style = MaterialTheme.typography.labelMedium,
          color = accentColor
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Custom cyber progress bar
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(Color(0xFF0F172A))
          .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
          .padding(1.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(profile.moodPercent)
            .clip(RoundedCornerShape(5.dp))
            .background(
              Brush.horizontalGradient(
                colors = listOf(
                  accentColor.copy(alpha = 0.7f),
                  accentColor
                )
              )
            )
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Sub-widget: Princesa la Gata + Audio Visualizer
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(Color.Black.copy(alpha = 0.5f))
          .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
          .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(30.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(accentColor.copy(alpha = 0.2f))
              .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Pets,
              contentDescription = "Princesa",
              tint = accentColor,
              modifier = Modifier.size(16.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Column {
            Text(
              text = "Princesa (Gata)",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White
            )
            Text(
              text = profile.catStatus,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 10.sp,
              color = accentColor
            )
          }
        }

        // Soundwave animated bars
        Row(
          horizontalArrangement = Arrangement.spacedBy(3.dp),
          verticalAlignment = Alignment.Bottom,
          modifier = Modifier.height(20.dp)
        ) {
          Box(
            modifier = Modifier
              .width(3.dp)
              .height(bar1Height.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(accentColor)
          )
          Box(
            modifier = Modifier
              .width(3.dp)
              .height(bar2Height.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(accentColor)
          )
          Box(
            modifier = Modifier
              .width(3.dp)
              .height(bar3Height.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(accentColor)
          )
          Box(
            modifier = Modifier
              .width(3.dp)
              .height(bar4Height.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(accentColor)
          )
        }
      }
    }
  }
}
