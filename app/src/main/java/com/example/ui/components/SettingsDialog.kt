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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun SettingsDialog(
  isLauraMode: Boolean,
  currentApiKey: String,
  onSaveApiKey: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary
  var inputKey by remember { mutableStateOf(currentApiKey) }
  var showKey by remember { mutableStateOf(false) }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.5.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
      color = Color(0xFF0F121C)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
      ) {
        // Title Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Key,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "CONFIGURACIÓN GEMINI",
              style = MaterialTheme.typography.titleMedium,
              color = accentColor
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Cerrar",
              tint = Color.LightGray
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Key Status Badge
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (currentApiKey.isNotBlank()) CyberEmerald.copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f))
            .border(
              1.dp,
              if (currentApiKey.isNotBlank()) CyberEmerald.copy(alpha = 0.4f) else Color(0xFFEF4444).copy(alpha = 0.4f),
              RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (currentApiKey.isNotBlank()) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            tint = if (currentApiKey.isNotBlank()) CyberEmerald else Color(0xFFEF4444),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (currentApiKey.isNotBlank()) "Enlace Gemini Activo (Modelo gemini-2.5-flash)" else "API Key no detectada. Ingresa tu clave para respuestas en vivo.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            fontSize = 11.sp
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Ingresa tu Gemini API Key de Google AI Studio:",
          style = MaterialTheme.typography.labelSmall,
          color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
          value = inputKey,
          onValueChange = { inputKey = it },
          placeholder = { Text("AIzaSy...", color = Color.Gray, fontSize = 12.sp) },
          textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = accentColor,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
          ),
          visualTransformation = if (showKey) VisualTransformation.None else PasswordVisualTransformation(),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_api_key"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color(0xFF334155),
            focusedContainerColor = Color(0xFF070913),
            unfocusedContainerColor = Color(0xFF070913)
          ),
          trailingIcon = {
            Text(
              text = if (showKey) "Ocultar" else "Ver",
              style = MaterialTheme.typography.labelSmall,
              color = accentColor,
              modifier = Modifier
                .padding(end = 12.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .testTag("btn_toggle_key_visibility")
            )
          }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "• Las respuestas utilizarán el modelo de última generación gemini-2.5-flash.\n• La clave se almacena cifrada en el almacenamiento local de tu dispositivo.\n• Cada persona (Sandra o Laura) envía sus instrucciones de sistema correspondientes.",
          style = MaterialTheme.typography.bodySmall,
          color = Color.Gray,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray)
          ) {
            Text("Cancelar", style = MaterialTheme.typography.labelSmall)
          }

          Spacer(modifier = Modifier.width(10.dp))

          Button(
            onClick = {
              onSaveApiKey(inputKey)
              onDismiss()
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = accentColor,
              contentColor = Color(0xFF0A0A0E)
            ),
            modifier = Modifier.testTag("btn_save_api_key")
          ) {
            Text("Guardar Clave", style = MaterialTheme.typography.labelMedium)
          }
        }
      }
    }
  }
}
