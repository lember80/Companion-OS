package com.example.data.repository

import com.example.data.model.CompanionProfile
import com.example.data.model.Kanji

object SkillExporter {

  /**
   * Generates Gemini System Instructions prompt (for Google Gemini Gems or AI Studio System Prompts).
   */
  fun generateGeminiSystemPrompt(profile: CompanionProfile): String {
    return """
# GEMINI SKILL SYSTEM INSTRUCTION: ${profile.name.uppercase()}
==============================================================

## IDENTITY & ROLE
- Name: ${profile.name}
- Title: ${profile.roleTitle}
- User: ${profile.userName}
- Context / Location: ${profile.location}

## CORE PERSONA
${profile.systemPrompt}

## BEHAVIORAL DIRECTIVES
1. Stay strictly in character as ${profile.name}.
2. Specialize in Japanese language instruction (${profile.kanjiTitle}) while maintaining high emotional connection with ${profile.userName}.
3. Balance educational rigor (grammar, romaji, kanji breakdown) with empathetic encouragement and personal motivation.
4. Keep responses concise, vivid, and punchy. Avoid robotic boilerplate.
5. If the user asks about Cat 'Princesa', acknowledge her current status: "${profile.catStatus}".
    """.trimIndent()
  }

  /**
   * Generates a complete Gemini Skill / Custom Gem specification in Markdown.
   */
  fun generateGeminiSkillMarkdown(profile: CompanionProfile): String {
    return """
# Gemini Custom Gem / Skill: ${profile.name}

### Metadata
- **Gem Name:** ${profile.name}
- **Role Tag:** ${profile.roleTitle}
- **System Spec:** ${profile.osTitle}
- **Target User:** ${profile.userName}
- **Connection Mode:** ${profile.connTitle} (${profile.connMetric})

---

### System Instructions
```text
${generateGeminiSystemPrompt(profile)}
```

---

### Recommended Quick Prompts
${profile.quickPrompts.joinToString("\n") { "- $it" }}

### Japanese Focus
- Section: ${profile.kanjiTitle}
- Cat Companion: Princesa (${profile.catStatus})
    """.trimIndent()
  }

  /**
   * Generates the standalone HTML + Tailwind + Gemini 2.5 Flash script requested by the user.
   */
  fun generateHtmlTemplate(profile: CompanionProfile, kanjis: List<Kanji>): String {
    val isSandra = profile.mode != com.example.data.model.CompanionMode.LAURA
    val themeClass = if (isSandra) "bg-space-grid sandra-mode" else "bg-cyber-grid laura-mode"
    val accentColor = if (isSandra) "amber" else "pink"
    val glassClass = if (isSandra) "glass-gold" else "glass-neon"
    val glowClass = if (isSandra) "glow-gold-text" else "glow-neon-text"

    val kanjiJson = kanjis.joinToString(",\n      ") {
      """{ char: "${it.char}", romaji: "${it.romaji}", meaning: "${it.meaning}", example: "${it.example.replace("\"", "\\\"")}" }"""
    }

    return """<!DOCTYPE html>
<html lang="es" class="h-full bg-slate-950">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Companion OS - ${profile.name}</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@400;600;800;900&family=Rajdhani:wght@500;600;700&family=Noto+Sans+JP:wght@400;700&display=swap" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/lucide@latest/dist/umd/lucide.min.js"></script>
  <style>
    .font-orbitron { font-family: 'Orbitron', sans-serif; }
    .font-rajdhani { font-family: 'Rajdhani', sans-serif; }
    .font-jp { font-family: 'Noto Sans JP', sans-serif; }
    .glass-gold { background: rgba(18, 16, 10, 0.78); backdrop-filter: blur(16px); border: 1px solid rgba(234, 179, 8, 0.35); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.5); }
    .glass-neon { background: rgba(24, 10, 26, 0.8); backdrop-filter: blur(16px); border: 1px solid rgba(236, 72, 153, 0.4); box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.6); }
    .glow-gold-text { text-shadow: 0 0 10px rgba(234, 179, 8, 0.6); }
    .glow-neon-text { text-shadow: 0 0 10px rgba(236, 72, 153, 0.8); }
    @keyframes barPulse { 0%, 100% { height: 4px; } 50% { height: 20px; } }
    .wave-bar { animation: barPulse 1.2s infinite ease-in-out; }
  </style>
</head>
<body class="h-full text-slate-100 font-rajdhani selection:bg-$accentColor-500 selection:text-black">
  <div class="min-h-screen $themeClass flex flex-col relative p-4">
    <!-- HEADER -->
    <header class="w-full px-4 py-3 border-b border-$accentColor-500/30 bg-black/50 backdrop-blur-md rounded-2xl flex items-center justify-between mb-4">
      <div class="flex items-center gap-3">
        <div class="w-10 h-10 rounded-lg border border-$accentColor-500/40 bg-$accentColor-500/10 text-$accentColor-400 font-orbitron font-extrabold text-xl flex items-center justify-center">
          ${profile.badge}
        </div>
        <div>
          <h1 class="font-orbitron font-black text-lg text-$accentColor-400 $glowClass">${profile.osTitle}</h1>
          <p class="text-xs text-slate-400 font-mono">STATUS: <span class="text-emerald-400">${profile.status}</span> | ${profile.location}</p>
        </div>
      </div>
      <div class="text-right font-mono text-xs text-$accentColor-300">
        ${profile.roleTitle}
      </div>
    </header>

    <!-- CONTENT GRID -->
    <main class="grid grid-cols-1 lg:grid-cols-12 gap-4 flex-1">
      <!-- LEFT: WIDGETS -->
      <section class="lg:col-span-4 flex flex-col gap-4">
        <!-- Neural Link -->
        <div class="$glassClass rounded-2xl p-4">
          <p class="text-xs font-orbitron text-$accentColor-300 uppercase">${profile.connTitle}</p>
          <p class="text-3xl font-orbitron font-extrabold text-white">${profile.connMetric}</p>
          <p class="text-xs text-slate-400 font-mono">Sincronización con ${profile.userName}</p>
        </div>
        <!-- Mood -->
        <div class="$glassClass rounded-2xl p-4 space-y-2">
          <div class="flex justify-between text-xs font-mono">
            <span>${profile.labelMood}</span>
            <span class="text-$accentColor-400 font-bold">${(profile.moodPercent * 100).toInt()}%</span>
          </div>
          <div class="w-full bg-slate-900 h-2.5 rounded-full overflow-hidden border border-slate-700">
            <div class="bg-$accentColor-500 h-full rounded-full" style="width: ${(profile.moodPercent * 100).toInt()}%;"></div>
          </div>
          <div class="pt-2 flex justify-between items-center text-xs text-slate-300">
            <span>Princesa (Gata): ${profile.catStatus}</span>
            <div class="flex gap-1 h-4 items-end">
              <span class="w-1 bg-$accentColor-400 wave-bar"></span>
              <span class="w-1 bg-$accentColor-400 wave-bar"></span>
              <span class="w-1 bg-$accentColor-400 wave-bar"></span>
            </div>
          </div>
        </div>
        <!-- Kanji Flashcard -->
        <div class="$glassClass rounded-2xl p-4 text-center cursor-pointer" onclick="nextKanji()">
          <p class="text-xs font-orbitron text-$accentColor-300 mb-2">${profile.kanjiTitle}</p>
          <div class="text-6xl font-jp font-bold text-white mb-2" id="kChar">${kanjis.firstOrNull()?.char ?: "夢"}</div>
          <p class="text-lg font-orbitron text-$accentColor-400" id="kRomaji">${kanjis.firstOrNull()?.romaji ?: "Yume"}</p>
          <p class="text-sm text-slate-300" id="kMean">${kanjis.firstOrNull()?.meaning ?: "Sueño"}</p>
        </div>
      </section>

      <!-- RIGHT: CHAT CONSOLE -->
      <section class="lg:col-span-8 flex flex-col $glassClass rounded-2xl overflow-hidden min-h-[450px]">
        <div class="p-3 border-b border-$accentColor-500/20 bg-black/40 flex justify-between items-center">
          <span class="font-orbitron text-xs font-bold text-$accentColor-300 uppercase">${profile.name} :: CONSOLA DIALOGO</span>
          <span class="text-xs font-mono text-emerald-400">GEMINI 2.5 FLASH</span>
        </div>
        <div id="chatBox" class="flex-1 p-4 overflow-y-auto space-y-3 font-rajdhani">
          <div class="p-3 bg-slate-900/80 border border-$accentColor-500/30 rounded-xl text-slate-200">
            <span class="font-orbitron font-bold text-$accentColor-400 text-xs">${profile.name}:</span>
            <p>${profile.initialMsg}</p>
          </div>
        </div>
        <form onsubmit="sendMsg(event)" class="p-3 border-t border-$accentColor-500/20 bg-black/60 flex gap-2">
          <input id="promptIn" type="text" placeholder="Escribe un mensaje..." class="flex-1 bg-slate-950 border border-slate-700 rounded-xl px-3 py-2 text-sm text-white focus:outline-none focus:border-$accentColor-400">
          <button type="submit" class="bg-$accentColor-500 hover:bg-$accentColor-400 text-slate-950 font-orbitron font-bold px-4 py-2 rounded-xl">Enviar</button>
        </form>
      </section>
    </main>
  </div>

  <script>
    lucide.createIcons();
    const kanjis = [
      $kanjiJson
    ];
    let kIdx = 0;
    function nextKanji() {
      kIdx = (kIdx + 1) % kanjis.length;
      document.getElementById('kChar').innerText = kanjis[kIdx].char;
      document.getElementById('kRomaji').innerText = kanjis[kIdx].romaji;
      document.getElementById('kMean').innerText = kanjis[kIdx].meaning;
    }

    async function sendMsg(e) {
      e.preventDefault();
      const inEl = document.getElementById('promptIn');
      const text = inEl.value.trim();
      if (!text) return;
      const box = document.getElementById('chatBox');
      box.innerHTML += `<div class="p-2.5 bg-$accentColor-500/20 border border-$accentColor-500/30 rounded-xl text-right"><span class="font-orbitron text-xs text-$accentColor-300 font-bold">${profile.userName}:</span><p>` + text + `</p></div>`;
      inEl.value = '';
      box.scrollTop = box.scrollHeight;

      try {
        const apiKey = localStorage.getItem('gemini_api_key') || "";
        const payload = {
          contents: [{ role: "user", parts: [{ text: text }] }],
          systemInstruction: { parts: [{ text: `${profile.systemPrompt.replace("\"", "\\\"")}` }] }
        };
        const res = await fetch(`https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=` + apiKey, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload)
        });
        const d = await res.json();
        const reply = d.candidates?.[0]?.content?.parts?.[0]?.text || "Respuesta recibida.";
        box.innerHTML += `<div class="p-2.5 bg-slate-900 border border-$accentColor-500/30 rounded-xl"><span class="font-orbitron text-xs text-$accentColor-400 font-bold">${profile.name}:</span><p>` + reply + `</p></div>`;
      } catch (err) {
        box.innerHTML += `<div class="text-xs text-red-400 font-mono text-center">Error: Verifica tu API Key en localStorage ('gemini_api_key').</div>`;
      }
      box.scrollTop = box.scrollHeight;
    }
  </script>
</body>
</html>
    """.trimIndent()
  }
}
