package com.example

import com.example.data.model.Kanji
import com.example.data.repository.KanjiRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KanjiRepositoryTest {

  private lateinit var repository: KanjiRepository

  private val testKanjis = listOf(
    Kanji(char = "夢", romaji = "Yume", meaning = "Sueño", example = "夢を叶える", level = "N3"),
    Kanji(char = "愛", romaji = "Ai", meaning = "Amor", example = "愛を込めて", level = "N4"),
    Kanji(char = "気", romaji = "Ki", meaning = "Espíritu", example = "元気", level = "N5")
  )

  @Before
  fun setUp() {
    repository = KanjiRepository(testKanjis)
  }

  @Test
  fun initialCard_isFirstElement() {
    val initial = repository.getCurrentCard()
    assertEquals("夢", initial.char)
    assertEquals(0, repository.currentIndex.value)
  }

  @Test
  fun nextCard_cyclesForward() {
    val second = repository.nextCard()
    assertEquals("愛", second.char)
    assertEquals(1, repository.currentIndex.value)

    val third = repository.nextCard()
    assertEquals("気", third.char)
    assertEquals(2, repository.currentIndex.value)

    // Should wrap around to first card
    val wrap = repository.nextCard()
    assertEquals("夢", wrap.char)
    assertEquals(0, repository.currentIndex.value)
  }

  @Test
  fun previousCard_cyclesBackward() {
    // Calling previous from index 0 should wrap to last card
    val last = repository.previousCard()
    assertEquals("気", last.char)
    assertEquals(2, repository.currentIndex.value)

    val second = repository.previousCard()
    assertEquals("愛", second.char)
    assertEquals(1, repository.currentIndex.value)
  }

  @Test
  fun filterByLevel_returnsMatchingKanjis() {
    val n3List = repository.filterByLevel("N3")
    assertEquals(1, n3List.size)
    assertEquals("夢", n3List[0].char)
  }
}
