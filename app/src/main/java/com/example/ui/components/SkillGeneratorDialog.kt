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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CompanionMode
import com.example.data.model.CompanionProfile
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun SkillGeneratorDialog(
  currentProfile: CompanionProfile,
  isLauraMode: Boolean,
  onApplySkill: (CompanionProfile) -> Unit,
  onDismiss: () -> Unit
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary

  var characterName by remember { mutableStateOf(currentProfile.name) }
  var roleTitle by remember { mutableStateOf(currentProfile.roleTitle) }
  var japaneseLevel by remember { mutableStateOf("N3 Intermedio") }
  var personalityTone by remember { mutableStateOf(if (isLauraMode) "Cyberpunk Dinámica" else "Ejecutiva Elegante") }
  var systemInstructionText by remember { mutableStateOf(currentProfile.systemPrompt) }
  var catStatusText by remember { mutableStateOf(currentProfile.catStatus) }

  val levels = listOf("N5 Básico", "N4 Elemental", "N3 Intermedio", "N2 Avanzado", "N1 Experto", "Slang Urbano")
  val tones = listOf("Ejecutiva Elegante", "Cyberpunk Dinámica", "Sensei Tradicional", "Tsundere Tierna")

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.5.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
      color = Color(0xFF0F121E)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Title Bar
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "GENERADOR DE SKILLS",
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

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Crea o personaliza la personalidad, nivel de japonés y prompts del sistema para tus interacciones con Gemini.",
          style = MaterialTheme.typography.bodySmall,
          color = Color.LightGray,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Name
        Text("Nombre del Personaje / Skill:", style = MaterialTheme.typography.labelSmall, color = accentColor)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = characterName,
          onValueChange = { characterName = it },
          textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_skill_name"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color(0xFF334155),
            focusedContainerColor = Color(0xFF070913),
            unfocusedContainerColor = Color(0xFF070913)
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Role
        Text("Título / Cargo en el HUD:", style = MaterialTheme.typography.labelSmall, color = accentColor)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = roleTitle,
          onValueChange = { roleTitle = it },
          textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color(0xFF334155),
            focusedContainerColor = Color(0xFF070913),
            unfocusedContainerColor = Color(0xFF070913)
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Japanese Level
        Text("Nivel de Japonés Focal:", style = MaterialTheme.typography.labelSmall, color = accentColor)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          levels.take(3).forEach { lvl ->
            FilterChip(
              selected = japaneseLevel == lvl,
              onClick = { japaneseLevel = lvl },
              label = { Text(lvl, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = accentColor,
                selectedLabelColor = Color(0xFF0F172A)
              )
            )
          }
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          levels.drop(3).forEach { lvl ->
            FilterChip(
              selected = japaneseLevel == lvl,
              onClick = { japaneseLevel = lvl },
              label = { Text(lvl, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = accentColor,
                selectedLabelColor = Color(0xFF0F172A)
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // System Instruction Prompt
        Text("System Prompt para la API de Gemini:", style = MaterialTheme.typography.labelSmall, color = accentColor)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = systemInstructionText,
          onValueChange = { systemInstructionText = it },
          textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontSize = 11.sp),
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 150.dp)
            .testTag("input_system_prompt"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color(0xFF334155),
            focusedContainerColor = Color(0xFF070913),
            unfocusedContainerColor = Color(0xFF070913)
          ),
          maxLines = 8
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Cat Companion status
        Text("Princesa la Gata (Estado de Audio):", style = MaterialTheme.typography.labelSmall, color = accentColor)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
          value = catStatusText,
          onValueChange = { catStatusText = it },
          textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = accentColor,
            unfocusedBorderColor = Color(0xFF334155),
            focusedContainerColor = Color(0xFF070913),
            unfocusedContainerColor = Color(0xFF070913)
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Apply Button
        Button(
          onClick = {
            val updatedProfile = currentProfile.copy(
              name = characterName,
              roleTitle = roleTitle,
              systemPrompt = systemInstructionText,
              catStatus = catStatusText,
              badge = characterName.firstOrNull()?.toString()?.uppercase() ?: "A"
            )
            onApplySkill(updatedProfile)
          },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = accentColor,
            contentColor = Color(0xFF0A0A0E)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("btn_apply_custom_skill")
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Activar Skill en Companion OS", style = MaterialTheme.typography.labelMedium)
        }
      }
    }
  }
}
