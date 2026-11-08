package com.example.data.model

data class Kanji(
  val char: String,
  val romaji: String,
  val meaning: String,
  val example: String,
  val level: String = "N3"
)

typealias KanjiItem = Kanji

val DefaultKanjiList: List<Kanji> = listOf(
  Kanji(
    char = "夢",
    romaji = "Yume",
    meaning = "Sueño / Visión de futuro",
    example = "夢を叶える (Yume wo kanaeru) - Hacer realidad un sueño.",
    level = "N3"
  ),
  Kanji(
    char = "愛",
    romaji = "Ai",
    meaning = "Amor / Afecto incondicional",
    example = "愛を込めて (Ai wo komete) - Con todo mi amor.",
    level = "N4"
  ),
  Kanji(
    char = "絆",
    romaji = "Kizuna",
    meaning = "Lazo / Vínculo humano fuerte",
    example = "強い絆 (Tsuyoi kizuna) - Un lazo inquebrantable.",
    level = "N3"
  ),
  Kanji(
    char = "光",
    romaji = "Hikari",
    meaning = "Luz / Resplandor de esperanza",
    example = "未来への光 (Mirai e no hikari) - La luz hacia el futuro.",
    level = "N4"
  ),
  Kanji(
    char = "勝",
    romaji = "Katsu",
    meaning = "Victoria / Superación",
    example = "自分に勝つ (Jibun ni katsu) - Vencerse a uno mismo.",
    level = "N3"
  ),
  Kanji(
    char = "道",
    romaji = "Michi",
    meaning = "Camino / Disciplina / Vía",
    example = "自分の道を進む (Jibun no michi wo susumu) - Seguir tu propio camino.",
    level = "N4"
  ),
  Kanji(
    char = "気",
    romaji = "Ki",
    meaning = "Espíritu / Energía vital",
    example = "元気を出して (Genki wo dashite) - ¡Ánimo y fuerza!",
    level = "N5"
  ),
  Kanji(
    char = "心",
    romaji = "Kokoro",
    meaning = "Corazón / Mente / Sentimiento",
    example = "心を一つにする (Kokoro wo hitotsu ni suru) - Unir los corazones.",
    level = "N4"
  ),
  Kanji(
    char = "力",
    romaji = "Chikara",
    meaning = "Fuerza / Potencia / Habilidad",
    example = "力を合わせる (Chikara wo awaseru) - Unir fuerzas.",
    level = "N5"
  )
)
