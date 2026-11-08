package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.CompanionProfile
import com.example.data.model.MessageSender
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun DialogueConsole(
  profile: CompanionProfile,
  isLauraMode: Boolean,
  messages: List<ChatMessage>,
  isLoadingAi: Boolean,
  onSendMessage: (String) -> Unit,
  onClearChat: () -> Unit,
  isSpeaking: Boolean = false,
  onSpeakMessage: ((String) -> Unit)? = null,
  onStopSpeaking: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary
  val cardBg = if (isLauraMode) Color(0xFF180A1A).copy(alpha = 0.88f) else Color(0xFF14120C).copy(alpha = 0.88f)

  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  LaunchedEffect(messages.size, isLoadingAi) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

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
        .padding(bottom = 8.dp)
    ) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.Black.copy(alpha = 0.5f))
          .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
          .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Terminal,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "CONSOLA DE DIÁLOGO AI",
            style = MaterialTheme.typography.labelMedium,
            color = accentColor
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (isSpeaking) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(accentColor.copy(alpha = 0.2f))
                .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                .clickable { onStopSpeaking?.invoke() }
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .testTag("btn_stop_speaking")
            ) {
              Icon(
                imageVector = Icons.Default.StopCircle,
                contentDescription = "Detener voz",
                tint = accentColor,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "HABLANDO",
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(CyberEmerald.copy(alpha = 0.15f))
              .border(1.dp, CyberEmerald.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "GEMINI 2.5 FLASH",
              style = MaterialTheme.typography.labelSmall,
              color = CyberEmerald,
              fontSize = 9.sp
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          IconButton(
            onClick = onClearChat,
            modifier = Modifier
              .size(28.dp)
              .testTag("btn_clear_chat")
          ) {
            Icon(
              imageVector = Icons.Default.DeleteOutline,
              contentDescription = "Limpiar Chat",
              tint = Color.LightGray.copy(alpha = 0.7f),
              modifier = Modifier.size(18.dp)
            )
          }
        }
      }

      // Messages Stream
      LazyColumn(
        state = listState,
        modifier = Modifier
          .fillMaxWidth()
          .heightIn(min = 260.dp, max = 380.dp)
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(messages, key = { it.id }) { msg ->
          when (msg.sender) {
            MessageSender.USER -> {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                Column(
                  horizontalAlignment = Alignment.End,
                  modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(14.dp).copy(bottomEnd = androidx.compose.foundation.shape.CornerSize(2.dp)))
                      .background(accentColor.copy(alpha = 0.2f))
                      .border(1.dp, accentColor.copy(alpha = 0.45f), RoundedCornerShape(14.dp).copy(bottomEnd = androidx.compose.foundation.shape.CornerSize(2.dp)))
                      .padding(10.dp)
                  ) {
                    Column {
                      Text(
                        text = "${msg.senderName} (Tú)",
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                      )
                      Spacer(modifier = Modifier.height(2.dp))
                      Text(
                        text = msg.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                      )
                    }
                  }
                  Text(
                    text = msg.formattedTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 2.dp, end = 4.dp)
                  )
                }
              }
            }

            MessageSender.COMPANION -> {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
              ) {
                // Avatar badge
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = profile.badge,
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor,
                    fontSize = 12.sp
                  )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(14.dp).copy(bottomStart = androidx.compose.foundation.shape.CornerSize(2.dp)))
                      .background(Color(0xFF0F172A).copy(alpha = 0.95f))
                      .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp).copy(bottomStart = androidx.compose.foundation.shape.CornerSize(2.dp)))
                      .padding(10.dp)
                  ) {
                    Column {
                      Text(
                        text = msg.senderName,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                      )
                      Spacer(modifier = Modifier.height(2.dp))
                      Text(
                        text = msg.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFF1F5F9)
                      )
                    }
                  }
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = 2.dp, start = 4.dp, end = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = msg.formattedTime,
                      style = MaterialTheme.typography.labelSmall,
                      color = Color.Gray,
                      fontSize = 9.sp
                    )

                    if (onSpeakMessage != null) {
                      Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                          .clip(RoundedCornerShape(6.dp))
                          .clickable { onSpeakMessage(msg.text) }
                          .padding(horizontal = 6.dp, vertical = 2.dp)
                          .testTag("btn_speak_msg_${msg.id}")
                      ) {
                        Icon(
                          imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                          contentDescription = "Escuchar voz",
                          tint = accentColor.copy(alpha = 0.85f),
                          modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                          text = "Voz",
                          style = MaterialTheme.typography.labelSmall,
                          color = accentColor.copy(alpha = 0.85f),
                          fontSize = 9.sp
                        )
                      }
                    }
                  }
                }
              }
            }

            MessageSender.SYSTEM -> {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
              ) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E1E24))
                    .border(1.dp, Color(0xFF333340), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                  Text(
                    text = msg.text,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF87171),
                    fontSize = 11.sp
                  )
                }
              }
            }
          }
        }

        // Typing loading state
        if (isLoadingAi) {
          item {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(start = 36.dp, top = 4.dp)
            ) {
              CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = accentColor
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "${profile.name} está respondiendo...",
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontSize = 11.sp
              )
            }
          }
        }
      }

      // Input Form
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = inputText,
          onValueChange = { inputText = it },
          placeholder = {
            Text(
              text = "Escribe tu mensaje en japonés o español...",
              style = MaterialTheme.typography.bodySmall,
              color = Color.Gray,
              fontSize = 12.sp
            )
          },
          textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
          modifier = Modifier
            .weight(1f)
            .testTag("input_chat_prompt"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color(0xFF334155),
            focusedContainerColor = Color(0xFF070913),
            unfocusedContainerColor = Color(0xFF070913),
            cursorColor = accentColor
          ),
          maxLines = 3,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
          keyboardActions = KeyboardActions(
            onSend = {
              if (inputText.isNotBlank() && !isLoadingAi) {
                onSendMessage(inputText)
                inputText = ""
              }
            }
          )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
          onClick = {
            if (inputText.isNotBlank() && !isLoadingAi) {
              onSendMessage(inputText)
              inputText = ""
            }
          },
          enabled = inputText.isNotBlank() && !isLoadingAi,
          modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (inputText.isNotBlank()) accentColor else Color(0xFF1E293B))
            .testTag("btn_send_chat")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Enviar",
            tint = if (inputText.isNotBlank()) Color(0xFF0A0A0E) else Color.Gray,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
