package com.example.data.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.UUID

private const val TAG = "TtsManager"

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

  private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)

  private val _isReady = MutableStateFlow(false)
  val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

  private val _isSpeaking = MutableStateFlow(false)
  val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      val ttsInstance = tts
      if (ttsInstance != null) {
        // Default to Spanish or Japanese if available
        val spanishLocale = Locale.forLanguageTag("es-ES")
        val result = ttsInstance.setLanguage(spanishLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
          ttsInstance.language = Locale.getDefault()
        }

        ttsInstance.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
          override fun onStart(utteranceId: String?) {
            _isSpeaking.value = true
          }

          override fun onDone(utteranceId: String?) {
            _isSpeaking.value = false
          }

          @Deprecated("Deprecated in Java")
          override fun onError(utteranceId: String?) {
            _isSpeaking.value = false
          }

          override fun onError(utteranceId: String?, errorCode: Int) {
            _isSpeaking.value = false
            Log.w(TAG, "TTS utterance error: code $errorCode")
          }
        })

        _isReady.value = true
        Log.d(TAG, "TextToSpeech initialized successfully")
      }
    } else {
      Log.e(TAG, "Failed to initialize TextToSpeech, status: $status")
      _isReady.value = false
    }
  }

  /**
   * Speaks the given text aloud with voice characteristics tailored to Sandra or Laura.
   */
  fun speak(text: String, isLauraMode: Boolean) {
    val ttsInstance = tts ?: return
    if (!_isReady.value) {
      Log.w(TAG, "TextToSpeech not ready yet")
      return
    }

    try {
      // Clean up markdown markers for cleaner speech synthesis
      val cleanedText = text
        .replace(Regex("[#*_`~]"), "")
        .replace(Regex("\\[.*?\\]\\(.*?\\)"), "")
        .trim()

      if (cleanedText.isBlank()) return

      // Detect predominantly Japanese text (Kanji, Hiragana, Katakana)
      val hasJapanese = containsJapaneseCharacters(cleanedText)
      if (hasJapanese) {
        val jpResult = ttsInstance.setLanguage(Locale.JAPANESE)
        if (jpResult == TextToSpeech.LANG_MISSING_DATA || jpResult == TextToSpeech.LANG_NOT_SUPPORTED) {
          // Fall back to Spanish / default if Japanese voice pack not installed
          ttsInstance.setLanguage(Locale.forLanguageTag("es-ES"))
        }
      } else {
        ttsInstance.setLanguage(Locale.forLanguageTag("es-ES"))
      }

      // Configure distinctive persona pitch and speed
      if (isLauraMode) {
        // Laura: High-energy, playful cyberpunk companion (higher pitch, faster pace)
        ttsInstance.setPitch(1.18f)
        ttsInstance.setSpeechRate(1.08f)
      } else {
        // Sandra: Serene, mature executive tutor (calm pitch, measured pace)
        ttsInstance.setPitch(0.95f)
        ttsInstance.setSpeechRate(0.92f)
      }

      val utteranceId = UUID.randomUUID().toString()
      ttsInstance.speak(cleanedText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    } catch (e: Exception) {
      Log.e(TAG, "Error speaking text", e)
      _isSpeaking.value = false
    }
  }

  /**
   * Speaks Japanese kanji and example specifically.
   */
  fun speakJapanese(kanjiChar: String, romaji: String, example: String, isLauraMode: Boolean) {
    val speechText = "$kanjiChar. $romaji. $example"
    val ttsInstance = tts ?: return
    if (!_isReady.value) return

    ttsInstance.setLanguage(Locale.JAPANESE)
    if (isLauraMode) {
      ttsInstance.setPitch(1.15f)
      ttsInstance.setSpeechRate(1.0f)
    } else {
      ttsInstance.setPitch(0.95f)
      ttsInstance.setSpeechRate(0.88f)
    }

    val utteranceId = UUID.randomUUID().toString()
    ttsInstance.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
  }

  fun stop() {
    try {
      tts?.stop()
      _isSpeaking.value = false
    } catch (e: Exception) {
      Log.e(TAG, "Error stopping TTS", e)
    }
  }

  fun shutdown() {
    try {
      tts?.stop()
      tts?.shutdown()
      tts = null
      _isReady.value = false
      _isSpeaking.value = false
    } catch (e: Exception) {
      Log.e(TAG, "Error shutting down TTS", e)
    }
  }

  private fun containsJapaneseCharacters(str: String): Boolean {
    for (char in str) {
      val code = char.code
      // Hiragana: 0x3040 - 0x309F, Katakana: 0x30A0 - 0x30FF, Kanji: 0x4E00 - 0x9FAF
      if ((code in 0x3040..0x309F) || (code in 0x30A0..0x30FF) || (code in 0x4E00..0x9FAF)) {
        return true
      }
    }
    return false
  }
}
