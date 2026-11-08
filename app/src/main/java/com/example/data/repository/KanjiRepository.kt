package com.example.data.repository

import com.example.data.model.DefaultKanjiList
import com.example.data.model.Kanji
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class KanjiRepository(
  initialKanjis: List<Kanji> = DefaultKanjiList
) {
  private val _kanjis = MutableStateFlow(initialKanjis)
  val kanjis: StateFlow<List<Kanji>> = _kanjis.asStateFlow()

  private val _currentIndex = MutableStateFlow(0)
  val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

  private val _currentKanji = MutableStateFlow(
    if (initialKanjis.isNotEmpty()) initialKanjis[0] else DefaultKanjiList[0]
  )
  val currentKanji: StateFlow<Kanji> = _currentKanji.asStateFlow()

  fun getCurrentCard(): Kanji {
    val list = _kanjis.value
    if (list.isEmpty()) return DefaultKanjiList[0]
    val index = _currentIndex.value % list.size
    return list[index]
  }

  fun nextCard(): Kanji {
    val list = _kanjis.value
    if (list.isEmpty()) return DefaultKanjiList[0]

    _currentIndex.update { (it + 1) % list.size }
    val newCard = list[_currentIndex.value]
    _currentKanji.value = newCard
    return newCard
  }

  fun previousCard(): Kanji {
    val list = _kanjis.value
    if (list.isEmpty()) return DefaultKanjiList[0]

    _currentIndex.update {
      if (it - 1 < 0) list.size - 1 else it - 1
    }
    val newCard = list[_currentIndex.value]
    _currentKanji.value = newCard
    return newCard
  }

  fun setCardIndex(index: Int): Kanji {
    val list = _kanjis.value
    if (list.isEmpty()) return DefaultKanjiList[0]

    val safeIndex = index.coerceIn(0, list.size - 1)
    _currentIndex.value = safeIndex
    val card = list[safeIndex]
    _currentKanji.value = card
    return card
  }

  fun filterByLevel(level: String): List<Kanji> {
    return _kanjis.value.filter { it.level.equals(level, ignoreCase = true) }
  }

  fun addCustomKanji(kanji: Kanji) {
    _kanjis.update { it + kanji }
  }
}
