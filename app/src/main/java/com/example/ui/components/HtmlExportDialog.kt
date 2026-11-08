package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary

@Composable
fun HtmlExportDialog(
  isLauraMode: Boolean,
  htmlCode: String,
  markdownSpec: String,
  onDismiss: () -> Unit
) {
  val accentColor = if (isLauraMode) NeonPinkPrimary else GoldPrimary
  val context = LocalContext.current
  var selectedTab by remember { mutableIntStateOf(0) } // 0: HTML Template, 1: Gemini Skill Markdown

  val currentContent = if (selectedTab == 0) htmlCode else markdownSpec

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .border(1.5.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
      color = Color(0xFF0C0F17)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp)
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Code,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "EXPORTADOR DE SKILL",
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

        // Tabs
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color.Transparent,
          contentColor = accentColor,
          indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
              modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
              color = accentColor
            )
          }
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = { Text("Formato HTML", style = MaterialTheme.typography.labelSmall) }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = { Text("Gemini Skill Prompt", style = MaterialTheme.typography.labelSmall) }
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = if (selectedTab == 0) {
            "Plantilla HTML5 + Tailwind + Gemini lista para abrir en cualquier navegador web o compartir."
          } else {
            "Instrucciones de sistema optimizadas para crear un Custom Gem en Google Gemini."
          },
          style = MaterialTheme.typography.bodySmall,
          color = Color.LightGray,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Code Viewer
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp, max = 260.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF05070D))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
        ) {
          SelectionContainer {
            Text(
              text = currentContent,
              style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                color = Color(0xFFE2E8F0)
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          OutlinedButton(
            onClick = {
              val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, currentContent)
                type = "text/plain"
              }
              val shareIntent = Intent.createChooser(sendIntent, "Compartir plantilla")
              context.startActivity(shareIntent)
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray)
          ) {
            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Compartir", style = MaterialTheme.typography.labelSmall)
          }

          Button(
            onClick = {
              val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
              val clip = ClipData.newPlainText("Skill Template", currentContent)
              clipboard.setPrimaryClip(clip)
              Toast.makeText(context, "¡Copiado al portapapeles!", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = accentColor,
              contentColor = Color(0xFF0A0A0E)
            ),
            modifier = Modifier.testTag("btn_copy_exported_code")
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Copiar Código", style = MaterialTheme.typography.labelMedium)
          }
        }
      }
    }
  }
}
