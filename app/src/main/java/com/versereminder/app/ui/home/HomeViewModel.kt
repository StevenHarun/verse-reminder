package com.versereminder.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.versereminder.app.data.repository.ScheduleRepository
import com.versereminder.app.data.repository.SettingsRepository
import com.versereminder.app.data.repository.VerseRepository
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.Schedule
import com.versereminder.app.domain.model.ThemeMode
import com.versereminder.app.domain.model.Verse
import com.versereminder.app.domain.model.VerseCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.debounce

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HomeViewModel(
    private val verseRepository: VerseRepository,
    private val scheduleRepository: ScheduleRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(VerseCategory.ALL)
    val selectedCategory: StateFlow<VerseCategory> = _selectedCategory.asStateFlow()

    private val _dailyVerse = MutableStateFlow<Verse?>(verseRepository.getInitialDailyVerse())
    val dailyVerse: StateFlow<Verse?> = _dailyVerse.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<Verse>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                verseRepository.searchVerses(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bibleVersion: StateFlow<BibleVersion> = settingsRepository.bibleVersionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BibleVersion.TB)

    val themeMode: StateFlow<ThemeMode> = settingsRepository.themeModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.DARK)

    val fontSizeScale: StateFlow<Float> = settingsRepository.fontSizeScaleFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    val fontType: StateFlow<AppFontType> = settingsRepository.fontTypeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppFontType.SERIF)

    val activeSchedules: StateFlow<List<Schedule>> = scheduleRepository.getAllSchedules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categoryVerses: StateFlow<List<Verse>> = _selectedCategory
        .flatMapLatest { category ->
            verseRepository.getVersesByCategory(category)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    init {
        loadDailyVerse()
    }

    fun loadDailyVerse() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val verse = verseRepository.getDailyVerse()
                _dailyVerse.value = verse
            } catch (e: Exception) {
                if (_dailyVerse.value == null) {
                    _dailyVerse.value = verseRepository.getInitialDailyVerse()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCategory(category: VerseCategory) {
        _selectedCategory.value = category
    }

    fun toggleBookmark(verseId: Long) {
        viewModelScope.launch {
            verseRepository.toggleBookmark(verseId)
            _dailyVerse.value?.let { current ->
                if (current.id == verseId) {
                    _dailyVerse.value = current.copy(isBookmarked = !current.isBookmarked)
                }
            }
        }
    }

    fun refreshDailyVerse() {
        viewModelScope.launch {
            _isLoading.value = true
            val random = verseRepository.getRandomVerseByCategory(VerseCategory.ALL)
            if (random != null) {
                _dailyVerse.value = random
            }
            _isLoading.value = false
        }
    }

    fun deleteCustomVerse(verseId: Long) {
        viewModelScope.launch {
            verseRepository.deleteCustomVerse(verseId)
            if (_dailyVerse.value?.id == verseId) {
                loadDailyVerse()
            }
        }
    }

    fun setBibleVersion(version: BibleVersion) {
        viewModelScope.launch {
            settingsRepository.setBibleVersion(version)
        }
    }

    fun toggleThemeMode() {
        viewModelScope.launch {
            val current = themeMode.value
            val next = if (current == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK
            settingsRepository.setThemeMode(next)
        }
    }

    class Factory(
        private val verseRepository: VerseRepository,
        private val scheduleRepository: ScheduleRepository,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(verseRepository, scheduleRepository, settingsRepository) as T
        }
    }
}
