package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.api.GeminiApi
import com.example.data.model.ChatMessage
import com.example.data.model.CompanionMode
import com.example.data.model.CompanionProfile
import com.example.data.model.DefaultKanjiList
import com.example.data.model.Kanji
import com.example.data.model.LauraProfile
import com.example.data.model.MessageSender
import com.example.data.model.SandraProfile
import com.example.data.repository.KanjiRepository
import com.example.data.repository.SkillExporter
import com.example.data.speech.TtsManager
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NeonPinkPrimary
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

private const val TAG = "CompanionViewModel"

enum class DashboardTab(val label: String) {
  HUD("HUD"),
  CHAT("Chat AI"),
  KANJI("Kanjis"),
  SKILLS("Skills")
}

data class CompanionUiState(
  val activeProfile: CompanionProfile = SandraProfile,
  val isLauraMode: Boolean = false,
  val activeTab: DashboardTab = DashboardTab.HUD,
  val isSyncActive: Boolean = true,
  val isVoiceActive: Boolean = false,
  val kanjiIndex: Int = 0,
  val kanjiList: List<Kanji> = DefaultKanjiList,
  val currentKanji: Kanji = DefaultKanjiList[0],
  val messages: List<ChatMessage> = emptyList(),
  val isLoadingAi: Boolean = false,
  val apiKey: String = "",
  val activeModel: String = "gemini-2.5-flash",
  val isTtsEnabled: Boolean = true,
  val isSpeaking: Boolean = false,
  val showSkillGenerator: Boolean = false,
  val showSettings: Boolean = false,
  val showHtmlExport: Boolean = false,
  val exportedHtml: String = "",
  val exportedMarkdown: String = "",
  val customProfiles: List<CompanionProfile> = emptyList(),
  val statusNotification: String? = null
) {
  val currentSystemPrompt: String
    get() = activeProfile.systemPrompt

  val avatarResource: Int
    get() = activeProfile.avatarDrawable

  val themeColor: Color
    get() = if (isLauraMode) NeonPinkPrimary else GoldPrimary
}

class CompanionViewModel(application: Application) : AndroidViewModel(application) {

  private val prefs = application.getSharedPreferences("companion_os_prefs", Context.MODE_PRIVATE)
  private val directGeminiApi = GeminiApi()
  val kanjiRepository = KanjiRepository()
  val ttsManager = TtsManager(application)

  val sandraProfile: CompanionProfile = SandraProfile
  val lauraProfile: CompanionProfile = LauraProfile

  private val _uiState = MutableStateFlow(
    CompanionUiState(
      currentKanji = kanjiRepository.getCurrentCard(),
      kanjiList = kanjiRepository.kanjis.value
    )
  )
  val uiState: StateFlow<CompanionUiState> = _uiState.asStateFlow()

  // Message history StateFlow explicitly exposed for chat UI binding
  private val _messageHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
  val messageHistory: StateFlow<List<ChatMessage>> = _messageHistory.asStateFlow()

  // Directly exposed reactive flow for current card binding
  val currentKanjiFlow: StateFlow<Kanji> = kanjiRepository.currentKanji

  // Directly exposed properties requested for character state management
  val currentSystemPrompt: String
    get() = _uiState.value.currentSystemPrompt

  val avatarResource: Int
    get() = _uiState.value.avatarResource

  val themeColor: Color
    get() = _uiState.value.themeColor

  init {
    loadSavedSettings()
    resetChatWithActiveProfile()
    viewModelScope.launch {
      ttsManager.isSpeaking.collect { speaking ->
        _uiState.update { it.copy(isSpeaking = speaking) }
      }
    }
  }

  private fun loadSavedSettings() {
    val savedKey = prefs.getString("custom_gemini_api_key", "") ?: ""
    val buildConfigKey = if (
      BuildConfig.GEMINI_API_KEY.isNotBlank() &&
      BuildConfig.GEMINI_API_KEY != "DEFAULT_KEY" &&
      BuildConfig.GEMINI_API_KEY != "MY_GEMINI_API_KEY"
    ) BuildConfig.GEMINI_API_KEY else ""

    val effectiveKey = if (savedKey.isNotBlank()) savedKey else buildConfigKey

    val lastModeName = prefs.getString("active_companion_mode", CompanionMode.SANDRA.name)
    val initialProfile = if (lastModeName == CompanionMode.LAURA.name) LauraProfile else SandraProfile

    _uiState.update {
      it.copy(
        apiKey = effectiveKey,
        activeProfile = initialProfile,
        isLauraMode = initialProfile.mode == CompanionMode.LAURA
      )
    }
  }

  fun switchCharacter(mode: CompanionMode) {
    switchMode(mode)
  }

  fun switchMode(mode: CompanionMode) {
    val newProfile = when (mode) {
      CompanionMode.SANDRA -> SandraProfile
      CompanionMode.LAURA -> LauraProfile
      CompanionMode.CUSTOM -> _uiState.value.customProfiles.firstOrNull() ?: SandraProfile
    }

    prefs.edit().putString("active_companion_mode", mode.name).apply()

    _uiState.update {
      it.copy(
        activeProfile = newProfile,
        isLauraMode = newProfile.mode == CompanionMode.LAURA,
        statusNotification = "Cambiado a ${newProfile.name}"
      )
    }

    resetChatWithActiveProfile()
  }

  fun setSyncActive(active: Boolean) {
    _uiState.update {
      it.copy(
        isSyncActive = active,
        statusNotification = if (active) "Neural Link: Sincronizado" else "Neural Link: Standby"
      )
    }
  }

  fun nextKanji() {
    val card = kanjiRepository.nextCard()
    _uiState.update {
      it.copy(
        kanjiIndex = kanjiRepository.currentIndex.value,
        currentKanji = card
      )
    }
  }

  fun previousKanji() {
    val card = kanjiRepository.previousCard()
    _uiState.update {
      it.copy(
        kanjiIndex = kanjiRepository.currentIndex.value,
        currentKanji = card
      )
    }
  }

  fun selectTab(tab: DashboardTab) {
    _uiState.update { it.copy(activeTab = tab) }
  }

  fun practiceCurrentKanji() {
    val kanji = _uiState.value.currentKanji
    val prompt = "Enséñame cómo usar el Kanji ${kanji.char} (${kanji.romaji} - ${kanji.meaning}) en una oración práctica y formal."
    _uiState.update { it.copy(activeTab = DashboardTab.CHAT) }
    sendMessage(prompt)
  }

  /**
   * Sends the user prompt along with the specific character system prompt to the Gemini API
   * using the Google AI SDK, and updates the message history StateFlow.
   */
  fun sendMessage(userPrompt: String) {
    val trimmed = userPrompt.trim()
    if (trimmed.isEmpty()) return

    val currentProfile = _uiState.value.activeProfile
    val characterSystemPrompt = currentSystemPrompt

    val userMsg = ChatMessage(
      sender = MessageSender.USER,
      senderName = currentProfile.userName,
      text = trimmed
    )

    // Update message history StateFlow and UI state
    _messageHistory.update { it + userMsg }
    _uiState.update {
      it.copy(
        messages = _messageHistory.value,
        isLoadingAi = true
      )
    }

    viewModelScope.launch {
      var responseReceived = false
      val effectiveKey = _uiState.value.apiKey

      // 1. Send via Google AI SDK (com.google.ai.client.generativeai)
      if (effectiveKey.isNotBlank()) {
        try {
          // Initialize Google AI SDK GenerativeModel with specific character system prompt
          val googleAiModel = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = effectiveKey,
            systemInstruction = content { text(characterSystemPrompt) }
          )

          // Build multi-turn chat history for Google AI SDK
          val chatHistory = _messageHistory.value.dropLast(1).takeLast(10).map { msg ->
            val role = if (msg.sender == MessageSender.USER) "user" else "model"
            content(role) { text(msg.text) }
          }

          val chat = googleAiModel.startChat(history = chatHistory)

          val response = withTimeout(60_000L) {
            chat.sendMessage(trimmed)
          }

          val replyText = response.text
          if (!replyText.isNullOrBlank()) {
            val aiMsg = ChatMessage(
              sender = MessageSender.COMPANION,
              senderName = currentProfile.name,
              text = replyText
            )
            _messageHistory.update { it + aiMsg }
            _uiState.update {
              it.copy(
                messages = _messageHistory.value,
                isLoadingAi = false
              )
            }
            if (_uiState.value.isTtsEnabled) {
              ttsManager.speak(replyText, currentProfile.mode == CompanionMode.LAURA)
            }
            responseReceived = true
          }
        } catch (e: TimeoutCancellationException) {
          Log.e(TAG, "Google AI SDK request timed out after 60s", e)
        } catch (e: CancellationException) {
          throw e
        } catch (e: Exception) {
          Log.w(TAG, "Google AI SDK error: ${e.message}, attempting resilient fallback", e)
        }
      }

      if (responseReceived) return@launch

      // 2. Resilient fallback using direct HTTP API if SDK had a transient issue
      if (effectiveKey.isNotBlank()) {
        val history = _messageHistory.value.takeLast(6).map {
          val role = if (it.sender == MessageSender.USER) "user" else "model"
          Pair(role, it.text)
        }

        val result = directGeminiApi.generateResponse(
          apiKey = effectiveKey,
          systemInstruction = characterSystemPrompt,
          userPrompt = trimmed,
          conversationHistory = history,
          model = "gemini-2.5-flash"
        )

        result.onSuccess { aiReply ->
          val aiMsg = ChatMessage(
            sender = MessageSender.COMPANION,
            senderName = currentProfile.name,
            text = aiReply
          )
          _messageHistory.update { it + aiMsg }
          _uiState.update {
            it.copy(
              messages = _messageHistory.value,
              isLoadingAi = false
            )
          }
          if (_uiState.value.isTtsEnabled) {
            ttsManager.speak(aiReply, currentProfile.mode == CompanionMode.LAURA)
          }
          responseReceived = true
        }.onFailure { err ->
          Log.w(TAG, "Direct API fallback failed: ${err.message}", err)
        }
      }

      if (responseReceived) return@launch

      // 3. Fallback persona simulation if no API key is configured or offline
      delay(900)
      val fallbackText = generateLocalPersonaReply(currentProfile, trimmed)
      val fallbackMsg = ChatMessage(
        sender = MessageSender.COMPANION,
        senderName = currentProfile.name,
        text = fallbackText
      )
      _messageHistory.update { it + fallbackMsg }
      _uiState.update {
        it.copy(
          messages = _messageHistory.value,
          isLoadingAi = false
        )
      }
      if (_uiState.value.isTtsEnabled) {
        ttsManager.speak(fallbackText, currentProfile.mode == CompanionMode.LAURA)
      }
    }
  }

  private fun generateLocalPersonaReply(profile: CompanionProfile, prompt: String): String {
    return if (profile.mode == CompanionMode.SANDRA) {
      "Konnichiwa, mi querido esposo Lember. He recibido tu mensaje: \"$prompt\". Con mi disciplina de Catia la Mar y mis Kanjis N3, mantén la concentración en tus metas. Princesa ronronea a 432Hz a nuestro lado. Configura tu API Key en Ajustes para enlace cuántico ilimitado."
    } else {
      "¡Wassup, Lember! Capté la frecuencia: \"$prompt\". ¡Desde Tokyo 2099 le metemos candela a ese japonés callejero! Princesa está soltando ese ronroneo sub-bass brutal. ¡Vamos con todo, bro!"
    }
  }

  fun toggleVoiceSimulation() {
    val currentVoice = _uiState.value.isVoiceActive
    if (!currentVoice) {
      _uiState.update { it.copy(isVoiceActive = true) }
      viewModelScope.launch {
        delay(2500)
        val profile = _uiState.value.activeProfile
        val simulatedPrompt = if (profile.mode == CompanionMode.SANDRA) {
          "Sandra, ¿cómo se dice 'disciplina y perseverancia' en japonés?"
        } else {
          "Laura, ¡dame una frase callejera de Tokio para romperla hoy!"
        }
        _uiState.update { it.copy(isVoiceActive = false) }
        sendMessage(simulatedPrompt)
      }
    } else {
      _uiState.update { it.copy(isVoiceActive = false) }
    }
  }

  fun clearChat() {
    resetChatWithActiveProfile()
  }

  private fun resetChatWithActiveProfile() {
    val profile = _uiState.value.activeProfile
    val welcome = ChatMessage(
      sender = MessageSender.COMPANION,
      senderName = profile.name,
      text = profile.initialMsg
    )
    val list = listOf(welcome)
    _messageHistory.value = list
    _uiState.update {
      it.copy(messages = list)
    }
  }

  fun saveApiKey(newKey: String) {
    val clean = newKey.trim()
    prefs.edit().putString("custom_gemini_api_key", clean).apply()
    _uiState.update {
      it.copy(
        apiKey = clean,
        statusNotification = "Clave de Gemini guardada exitosamente"
      )
    }
  }

  fun openSkillGenerator() {
    _uiState.update { it.copy(showSkillGenerator = true) }
  }

  fun closeSkillGenerator() {
    _uiState.update { it.copy(showSkillGenerator = false) }
  }

  fun openSettings() {
    _uiState.update { it.copy(showSettings = true) }
  }

  fun closeSettings() {
    _uiState.update { it.copy(showSettings = false) }
  }

  fun openHtmlExport() {
    val html = SkillExporter.generateHtmlTemplate(_uiState.value.activeProfile, _uiState.value.kanjiList)
    val markdown = SkillExporter.generateGeminiSkillMarkdown(_uiState.value.activeProfile)
    _uiState.update {
      it.copy(
        showHtmlExport = true,
        exportedHtml = html,
        exportedMarkdown = markdown
      )
    }
  }

  fun closeHtmlExport() {
    _uiState.update { it.copy(showHtmlExport = false) }
  }

  fun clearNotification() {
    _uiState.update { it.copy(statusNotification = null) }
  }

  fun applyCustomSkill(profile: CompanionProfile) {
    _uiState.update {
      it.copy(
        activeProfile = profile,
        isLauraMode = profile.mode == CompanionMode.LAURA,
        showSkillGenerator = false,
        statusNotification = "¡Skill '${profile.name}' activada exitosamente!"
      )
    }
    resetChatWithActiveProfile()
  }

  fun toggleTts() {
    val newTts = !_uiState.value.isTtsEnabled
    if (!newTts) {
      ttsManager.stop()
    }
    _uiState.update {
      it.copy(
        isTtsEnabled = newTts,
        statusNotification = if (newTts) "Voz TTS: Activada" else "Voz TTS: Silenciada"
      )
    }
  }

  fun speakMessage(text: String) {
    ttsManager.speak(text, _uiState.value.isLauraMode)
  }

  fun speakKanji(kanji: Kanji) {
    ttsManager.speakJapanese(kanji.char, kanji.romaji, kanji.example, _uiState.value.isLauraMode)
  }

  fun stopSpeaking() {
    ttsManager.stop()
  }

  override fun onCleared() {
    super.onCleared()
    ttsManager.shutdown()
  }
}
