package com.versereminder.app.ui.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.versereminder.app.data.repository.SettingsRepository
import com.versereminder.app.data.repository.VerseRepository
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Verse
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val verseRepository: VerseRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val bookmarkedVerses: StateFlow<List<Verse>> = verseRepository.getBookmarkedVerses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bibleVersion: StateFlow<BibleVersion> = settingsRepository.bibleVersionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BibleVersion.TB)

    val fontSizeScale: StateFlow<Float> = settingsRepository.fontSizeScaleFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    val fontType: StateFlow<AppFontType> = settingsRepository.fontTypeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppFontType.SERIF)

    fun toggleBookmark(verseId: Long) {
        viewModelScope.launch {
            verseRepository.toggleBookmark(verseId)
        }
    }

    fun addCustomVerse(
        reference: String,
        textId: String,
        textEn: String = "",
        category: com.versereminder.app.domain.model.VerseCategory = com.versereminder.app.domain.model.VerseCategory.HOPE
    ) {
        viewModelScope.launch {
            verseRepository.addCustomVerse(reference, textId, textEn, category)
        }
    }

    fun deleteCustomVerse(verseId: Long) {
        viewModelScope.launch {
            verseRepository.deleteCustomVerse(verseId)
        }
    }

    class Factory(
        private val verseRepository: VerseRepository,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BookmarkViewModel(verseRepository, settingsRepository) as T
        }
    }
}
