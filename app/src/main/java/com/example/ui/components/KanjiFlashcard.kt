package com.example.ui.components

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CompanionProfile
import com.example.data.model.Kanji
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun KanjiFlashcard(
  profile: CompanionProfile,
  isLauraMode: Boolean,
  kanji: Kanji,
  currentIndex: Int = 0,
  totalCount: Int = 9,
  onNextKanji: () -> Unit,
  onPreviousKanji: (() -> Unit)? = null,
  onPracticeInChat: () -> Unit,
  onSpeakKanji: ((Kanji) -> Unit)? = null,
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
            imageVector = Icons.Default.Book,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = profile.kanjiTitle,
            style = MaterialTheme.typography.labelMedium,
            color = accentColor
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "${currentIndex + 1}/$totalCount",
            style = MaterialTheme.typography.labelSmall,
            color = Color.LightGray,
            fontSize = 11.sp,
            modifier = Modifier.padding(end = 6.dp)
          )

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(accentColor.copy(alpha = 0.15f))
              .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
              .padding(horizontal = 7.dp, vertical = 2.dp)
          ) {
            Text(
              text = "${kanji.level} MASTER",
              style = MaterialTheme.typography.labelSmall,
              color = accentColor
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Interactive Kanji Card
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(Color.Black.copy(alpha = 0.55f))
          .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
          .clickable { onNextKanji() }
          .padding(14.dp)
          .testTag("card_kanji_interactive")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          if (onPreviousKanji != null) {
            IconButton(
              onClick = onPreviousKanji,
              modifier = Modifier.size(28.dp).testTag("btn_kanji_prev")
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Anterior Kanji",
                tint = accentColor.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
              )
            }
          } else {
            Spacer(modifier = Modifier.size(28.dp))
          }

          IconButton(
            onClick = onNextKanji,
            modifier = Modifier.size(28.dp).testTag("btn_kanji_next")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = "Siguiente Kanji",
              tint = accentColor.copy(alpha = 0.8f),
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Large Kanji Character
          Text(
            text = kanji.char,
            fontSize = 62.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
          )

          Text(
            text = kanji.romaji,
            style = MaterialTheme.typography.titleMedium,
            color = accentColor
          )

          Text(
            text = kanji.meaning,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Example box with audio button
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF0A0F1D))
              .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
              .padding(8.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Ejemplo & Pronunciación:",
                  style = MaterialTheme.typography.labelSmall,
                  color = accentColor,
                  fontWeight = FontWeight.Bold
                )

                if (onSpeakKanji != null) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(accentColor.copy(alpha = 0.15f))
                      .clickable { onSpeakKanji(kanji) }
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                      .testTag("btn_speak_kanji")
                  ) {
                    Icon(
                      imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                      contentDescription = "Escuchar pronunciación",
                      tint = accentColor,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                      text = "Escuchar",
                      style = MaterialTheme.typography.labelSmall,
                      color = accentColor,
                      fontSize = 10.sp
                    )
                  }
                }
              }
              Spacer(modifier = Modifier.height(3.dp))
              Text(
                text = kanji.example,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Practice Button
      Button(
        onClick = onPracticeInChat,
        colors = ButtonDefaults.buttonColors(
          containerColor = accentColor.copy(alpha = 0.2f),
          contentColor = accentColor
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
          .testTag("btn_practice_kanji")
      ) {
        Icon(
          imageVector = Icons.Default.AutoAwesome,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Practicar este Kanji en Chat",
          style = MaterialTheme.typography.labelMedium
        )
      }
    }
  }
}
