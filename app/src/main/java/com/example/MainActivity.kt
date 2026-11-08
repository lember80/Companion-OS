package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AvatarCenterpiece
import com.example.ui.components.CyberHudHeader
import com.example.ui.components.DialogueConsole
import com.example.ui.components.HolographicBottomNav
import com.example.ui.components.HtmlExportDialog
import com.example.ui.components.KanjiFlashcard
import com.example.ui.components.MoodStatusCard
import com.example.ui.components.NeuralLinkCard
import com.example.ui.components.SettingsDialog
import com.example.ui.components.SkillGeneratorDialog
import com.example.ui.theme.CompanionOSTheme
import com.example.ui.theme.CyberEmerald
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary
import com.example.ui.viewmodel.CompanionViewModel
import com.example.ui.viewmodel.DashboardTab
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

  private val viewModel: CompanionViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    com.example.ui.util.configureAppCheck(this, intent)
    enableEdgeToEdge()

    setContent {
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()

      CompanionOSTheme(isLauraMode = uiState.isLauraMode) {
        CompanionDashboardScreen(
          viewModel = viewModel,
          modifier = Modifier.fillMaxSize()
        )
      }
    }
  }
}

@Composable
fun CompanionDashboardScreen(
  viewModel: CompanionViewModel,
  modifier: Modifier = Modifier
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val isLaura = uiState.isLauraMode
  val accentColor = if (isLaura) NeonPinkPrimary else GoldPrimary

  // Dynamic Background Gradient
  val bgGradientColor1 by animateColorAsState(
    targetValue = if (isLaura) Color(0xFF1E0722) else Color(0xFF1F1705),
    animationSpec = tween(600),
    label = "bg1"
  )
  val bgGradientColor2 by animateColorAsState(
    targetValue = if (isLaura) Color(0xFF09040D) else Color(0xFF0A0A0E),
    animationSpec = tween(600),
    label = "bg2"
  )

  // Auto-dismiss notification after 3 seconds
  LaunchedEffect(uiState.statusNotification) {
    if (uiState.statusNotification != null) {
      delay(3000)
      viewModel.clearNotification()
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFF07070B),
    bottomBar = {
      HolographicBottomNav(
        currentTab = uiState.activeTab,
        isLauraMode = isLaura,
        onTabSelected = { viewModel.selectTab(it) },
        onOpenSettings = { viewModel.openSettings() }
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .statusBarsPadding()
        .background(
          Brush.radialGradient(
            colors = listOf(bgGradientColor1, bgGradientColor2),
            center = Offset(500f, 300f),
            radius = 1200f
          )
        )
        // Cyber Grid Lines decoration
        .drawBehind {
          val step = 48.dp.toPx()
          val gridColor = accentColor.copy(alpha = 0.04f)
          var x = 0f
          while (x < size.width) {
            drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
            x += step
          }
          var y = 0f
          while (y < size.height) {
            drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
            y += step
          }
        }
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 720.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // HUD Header with character switcher
          CyberHudHeader(
            profile = uiState.activeProfile,
            isLauraMode = isLaura,
            onSwitchMode = { viewModel.switchMode(it) },
            onOpenSkillGenerator = { viewModel.openSkillGenerator() },
            onOpenHtmlExport = { viewModel.openHtmlExport() },
            onOpenSettings = { viewModel.openSettings() },
            isTtsEnabled = uiState.isTtsEnabled,
            onToggleTts = { viewModel.toggleTts() }
          )

          // Status Notification Toast
          AnimatedVisibility(
            visible = uiState.statusNotification != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(accentColor.copy(alpha = 0.15f))
                .border(1.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = uiState.statusNotification ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontSize = 11.sp
              )
            }
          }

          // Floating System Telemetry Bar (Core Temp & Power)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Color.Black.copy(alpha = 0.45f))
              .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
              .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.DeviceThermostat,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "CORE TEMP: 36.5°C",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray,
                fontSize = 10.sp
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.ElectricBolt,
                contentDescription = null,
                tint = CyberEmerald,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "POWER: 98% (STABLE)",
                style = MaterialTheme.typography.labelSmall,
                color = CyberEmerald,
                fontSize = 10.sp
              )
            }
          }

          // Animated Content Based on Active Tab
          AnimatedContent(
            targetState = uiState.activeTab,
            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) },
            label = "tab_content_transition"
          ) { tab ->
            when (tab) {
              DashboardTab.HUD -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  // Centerpiece Avatar & Voice
                  AvatarCenterpiece(
                    profile = uiState.activeProfile,
                    isLauraMode = isLaura,
                    isVoiceActive = uiState.isVoiceActive,
                    onToggleVoice = { viewModel.toggleVoiceSimulation() },
                    onQuickPromptSelected = { prompt ->
                      viewModel.selectTab(DashboardTab.CHAT)
                      viewModel.sendMessage(prompt)
                    }
                  )

                  // Neural Link & Synchronization
                  NeuralLinkCard(
                    profile = uiState.activeProfile,
                    isLauraMode = isLaura,
                    isSyncActive = uiState.isSyncActive,
                    onSyncToggle = { viewModel.setSyncActive(it) }
                  )

                  // Emotional State & Princesa the Cat
                  MoodStatusCard(
                    profile = uiState.activeProfile,
                    isLauraMode = isLaura
                  )

                  // Kanji Card Preview
                  KanjiFlashcard(
                    profile = uiState.activeProfile,
                    isLauraMode = isLaura,
                    kanji = uiState.currentKanji,
                    currentIndex = uiState.kanjiIndex,
                    totalCount = uiState.kanjiList.size,
                    onNextKanji = { viewModel.nextKanji() },
                    onPreviousKanji = { viewModel.previousKanji() },
                    onPracticeInChat = { viewModel.practiceCurrentKanji() },
                    onSpeakKanji = { kanji -> viewModel.speakKanji(kanji) }
                  )
                }
              }

              DashboardTab.CHAT -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  // Direct Gemini AI Dialogue Console
                  DialogueConsole(
                    profile = uiState.activeProfile,
                    isLauraMode = isLaura,
                    messages = uiState.messages,
                    isLoadingAi = uiState.isLoadingAi,
                    onSendMessage = { prompt -> viewModel.sendMessage(prompt) },
                    onClearChat = { viewModel.clearChat() },
                    isSpeaking = uiState.isSpeaking,
                    onSpeakMessage = { text -> viewModel.speakMessage(text) },
                    onStopSpeaking = { viewModel.stopSpeaking() }
                  )
                }
              }

              DashboardTab.KANJI -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  // Full Kanji Study Flashcard Deck
                  KanjiFlashcard(
                    profile = uiState.activeProfile,
                    isLauraMode = isLaura,
                    kanji = uiState.currentKanji,
                    currentIndex = uiState.kanjiIndex,
                    totalCount = uiState.kanjiList.size,
                    onNextKanji = { viewModel.nextKanji() },
                    onPreviousKanji = { viewModel.previousKanji() },
                    onPracticeInChat = { viewModel.practiceCurrentKanji() },
                    onSpeakKanji = { kanji -> viewModel.speakKanji(kanji) }
                  )

                  // Quick prompt cards for Japanese mastery
                  Surface(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(16.dp))
                      .border(1.dp, accentColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
                    color = Color.Black.copy(alpha = 0.7f)
                  ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                      Text(
                        text = "FRASES CLAVE CON ${uiState.activeProfile.name.uppercase()}",
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor
                      )
                      Spacer(modifier = Modifier.height(8.dp))
                      uiState.activeProfile.quickPrompts.forEach { qp ->
                        Box(
                          modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                        ) {
                          Text(
                            text = qp,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                          )
                        }
                      }
                    }
                  }
                }
              }

              DashboardTab.SKILLS -> {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  Surface(
                    modifier = Modifier
                      .fillMaxWidth()
                      .clip(RoundedCornerShape(16.dp))
                      .border(1.5.dp, accentColor.copy(alpha = 0.45f), RoundedCornerShape(16.dp)),
                    color = Color.Black.copy(alpha = 0.75f)
                  ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                      Text(
                        text = "STUDIO DE SKILLS & EXPORTACIÓN",
                        style = MaterialTheme.typography.titleMedium,
                        color = accentColor
                      )
                      Spacer(modifier = Modifier.height(6.dp))
                      Text(
                        text = "Configura prompts del sistema, exporta especificaciones de Custom Gems para Google Gemini o descarga la plantilla web HTML5 interactiva.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray
                      )
                      Spacer(modifier = Modifier.height(14.dp))
                      SkillGeneratorDialog(
                        currentProfile = uiState.activeProfile,
                        isLauraMode = isLaura,
                        onApplySkill = { customProfile -> viewModel.applyCustomSkill(customProfile) },
                        onDismiss = { viewModel.selectTab(DashboardTab.HUD) }
                      )
                    }
                  }
                }
              }
            }
          }

          // Footer HUD Info
          HudFooter(
            accentColor = accentColor,
            location = uiState.activeProfile.location
          )

          Spacer(modifier = Modifier.height(14.dp))
        }
      }

      // Dialogs
      if (uiState.showSkillGenerator) {
        SkillGeneratorDialog(
          currentProfile = uiState.activeProfile,
          isLauraMode = isLaura,
          onApplySkill = { customProfile -> viewModel.applyCustomSkill(customProfile) },
          onDismiss = { viewModel.closeSkillGenerator() }
        )
      }

      if (uiState.showSettings) {
        SettingsDialog(
          isLauraMode = isLaura,
          currentApiKey = uiState.apiKey,
          onSaveApiKey = { newKey -> viewModel.saveApiKey(newKey) },
          onDismiss = { viewModel.closeSettings() }
        )
      }

      if (uiState.showHtmlExport) {
        HtmlExportDialog(
          isLauraMode = isLaura,
          htmlCode = uiState.exportedHtml,
          markdownSpec = uiState.exportedMarkdown,
          onDismiss = { viewModel.closeHtmlExport() }
        )
      }
    }
  }
}

@Composable
fun HudFooter(
  accentColor: Color,
  location: String,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
    color = Color.Black.copy(alpha = 0.5f)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "LOC: ${location.uppercase()}",
          style = MaterialTheme.typography.labelSmall,
          color = Color.Gray,
          fontSize = 10.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "LANG: JP / ES",
          style = MaterialTheme.typography.labelSmall,
          color = Color.Gray,
          fontSize = 10.sp
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "GEMINI 2.5 FLASH",
          style = MaterialTheme.typography.labelSmall,
          color = accentColor.copy(alpha = 0.8f),
          fontSize = 10.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(CyberEmerald)
        )
      }
    }
  }
}
