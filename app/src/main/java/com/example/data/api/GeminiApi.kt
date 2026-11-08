package com.example.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiApi(
  private val defaultModel: String = "gemini-2.5-flash"
) {
  private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

  suspend fun generateResponse(
    apiKey: String,
    systemInstruction: String,
    userPrompt: String,
    conversationHistory: List<Pair<String, String>> = emptyList(),
    model: String = defaultModel
  ): Result<String> = withContext(Dispatchers.IO) {
    if (apiKey.isBlank()) {
      return@withContext Result.failure(
        IllegalStateException("API Key no configurada. Ingresa tu clave en Ajustes (ícono de engranaje) o configúrala en el panel Secrets.")
      )
    }

    try {
      val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

      val jsonBody = JSONObject().apply {
        // System instruction
        put("systemInstruction", JSONObject().apply {
          put("parts", JSONArray().apply {
            put(JSONObject().apply {
              put("text", systemInstruction)
            })
          })
        })

        // Contents (conversation history + latest message)
        val contentsArray = JSONArray()
        for ((role, text) in conversationHistory.takeLast(10)) {
          val geminiRole = if (role.equals("user", ignoreCase = true)) "user" else "model"
          contentsArray.put(JSONObject().apply {
            put("role", geminiRole)
            put("parts", JSONArray().apply {
              put(JSONObject().apply { put("text", text) })
            })
          })
        }

        // Add current prompt
        contentsArray.put(JSONObject().apply {
          put("role", "user")
          put("parts", JSONArray().apply {
            put(JSONObject().apply { put("text", userPrompt) })
          })
        })

        put("contents", contentsArray)

        // Generation config for conversational responsiveness
        put("generationConfig", JSONObject().apply {
          put("temperature", 0.7)
          put("maxOutputTokens", 800)
        })
      }

      val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
      val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

      val response = client.newCall(request).execute()
      val responseString = response.body?.string() ?: ""

      if (!response.isSuccessful) {
        val errorMsg = try {
          val errorJson = JSONObject(responseString)
          errorJson.optJSONObject("error")?.optString("message") ?: "Error HTTP ${response.code}"
        } catch (_: Exception) {
          "Error HTTP ${response.code}: $responseString"
        }
        return@withContext Result.failure(Exception(errorMsg))
      }

      val responseJson = JSONObject(responseString)
      val candidates = responseJson.optJSONArray("candidates")
      if (candidates != null && candidates.length() > 0) {
        val candidate = candidates.getJSONObject(0)
        val content = candidate.optJSONObject("content")
        val parts = content?.optJSONArray("parts")
        if (parts != null && parts.length() > 0) {
          val text = parts.getJSONObject(0).optString("text", "")
          return@withContext Result.success(text)
        }
      }

      Result.failure(Exception("No se recibió respuesta estructurada del modelo."))
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
