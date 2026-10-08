package com.versereminder.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.versereminder.app.data.local.dao.HistoryWithVerse
import com.versereminder.app.data.repository.SettingsRepository
import com.versereminder.app.data.repository.VerseRepository
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val verseRepository: VerseRepository,
    private val settingsRepository: SettingsRepository,
    private val onClearHistoryCallback: suspend () -> Unit
) : ViewModel() {

    val historyList: StateFlow<List<HistoryWithVerse>> = verseRepository.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bibleVersion: StateFlow<BibleVersion> = settingsRepository.bibleVersionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BibleVersion.TB)

    val fontSizeScale: StateFlow<Float> = settingsRepository.fontSizeScaleFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    val fontType: StateFlow<AppFontType> = settingsRepository.fontTypeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppFontType.SERIF)

    fun clearHistory() {
        viewModelScope.launch {
            onClearHistoryCallback()
        }
    }

    class Factory(
        private val verseRepository: VerseRepository,
        private val settingsRepository: SettingsRepository,
        private val onClearHistory: suspend () -> Unit
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HistoryViewModel(verseRepository, settingsRepository, onClearHistory) as T
        }
    }
}
