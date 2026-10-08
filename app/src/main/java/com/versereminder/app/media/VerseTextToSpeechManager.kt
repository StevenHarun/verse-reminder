package com.versereminder.app.media

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Verse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class VerseTextToSpeechManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playingVerseId = MutableStateFlow<Long?>(null)
    val playingVerseId: StateFlow<Long?> = _playingVerseId.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            setupProgressListener()
        } else {
            Log.e("VerseTTS", "TextToSpeech initialization failed with status: $status")
            isInitialized = false
        }
    }

    private fun setupProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isPlaying.value = true
            }

            override fun onDone(utteranceId: String?) {
                _isPlaying.value = false
                _playingVerseId.value = null
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isPlaying.value = false
                _playingVerseId.value = null
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _isPlaying.value = false
                _playingVerseId.value = null
            }
        })
    }

    fun toggleSpeak(verse: Verse, bibleVersion: BibleVersion) {
        if (_isPlaying.value && _playingVerseId.value == verse.id) {
            stop()
        } else {
            speak(verse, bibleVersion)
        }
    }

    fun speak(verse: Verse, bibleVersion: BibleVersion) {
        if (!isInitialized) {
            // Re-initialize if needed
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isInitialized = true
                    setupProgressListener()
                    executeSpeak(verse, bibleVersion)
                }
            }
            return
        }

        executeSpeak(verse, bibleVersion)
    }

    private fun executeSpeak(verse: Verse, bibleVersion: BibleVersion) {
        val ttsInstance = tts ?: return
        val verseText = verse.getTextForVersion(bibleVersion)

        // Select speech language according to Bible version
        val targetLocale = when (bibleVersion) {
            BibleVersion.WEB, BibleVersion.KJV, BibleVersion.NIV, BibleVersion.ESV -> Locale.US
            else -> Locale("id", "ID")
        }

        val result = ttsInstance.setLanguage(targetLocale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to default or English
            ttsInstance.setLanguage(Locale.getDefault())
        }

        // Peaceful, clear recitation pacing
        ttsInstance.setSpeechRate(0.92f)
        ttsInstance.setPitch(1.0f)

        val spokenText = if (targetLocale.language == "id") {
            "Kitab ${verse.reference}. $verseText"
        } else {
            "Scripture from ${verse.reference}. $verseText"
        }

        val utteranceId = "verse_${verse.id}_${System.currentTimeMillis()}"
        _playingVerseId.value = verse.id
        _isPlaying.value = true

        ttsInstance.speak(
            spokenText,
            TextToSpeech.QUEUE_FLUSH,
            null,
            utteranceId
        )
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (_: Exception) {}
        _isPlaying.value = false
        _playingVerseId.value = null
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
        isInitialized = false
        _isPlaying.value = false
        _playingVerseId.value = null
    }
}
