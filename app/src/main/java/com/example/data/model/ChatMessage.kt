package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class MessageSender {
  USER,
  COMPANION,
  SYSTEM
}

data class ChatMessage(
  val id: String = UUID.randomUUID().toString(),
  val sender: MessageSender,
  val senderName: String,
  val text: String,
  val timestamp: Long = System.currentTimeMillis()
) {
  val formattedTime: String
    get() {
      val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
      return sdf.format(Date(timestamp))
    }
}
