package com.versereminder.app.ui.readingplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.versereminder.app.data.repository.ReadingPlanRepository
import com.versereminder.app.data.repository.SettingsRepository
import com.versereminder.app.domain.model.AppFontType
import com.versereminder.app.domain.model.BibleVersion
import com.versereminder.app.domain.model.ReadingPlan
import com.versereminder.app.domain.model.ReadingPlanProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReadingPlanViewModel(
    private val readingPlanRepository: ReadingPlanRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val allPlans: List<ReadingPlan> = readingPlanRepository.getAllPlans()

    val progressMap: StateFlow<Map<String, ReadingPlanProgress>> = readingPlanRepository.getAllProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private val _selectedPlanId = MutableStateFlow(allPlans.firstOrNull()?.id ?: "peace_7d")
    val selectedPlanId: StateFlow<String> = _selectedPlanId.asStateFlow()

    private val _selectedDay = MutableStateFlow(1)
    val selectedDay: StateFlow<Int> = _selectedDay.asStateFlow()

    val bibleVersion: StateFlow<BibleVersion> = settingsRepository.bibleVersionFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BibleVersion.TB)

    val fontSizeScale: StateFlow<Float> = settingsRepository.fontSizeScaleFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)

    val fontType: StateFlow<AppFontType> = settingsRepository.fontTypeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppFontType.SERIF)

    fun selectPlan(planId: String) {
        _selectedPlanId.value = planId
        val progress = progressMap.value[planId]
        val nextDay = progress?.currentDay ?: 1
        _selectedDay.value = nextDay
    }

    fun selectDay(dayNumber: Int) {
        _selectedDay.value = dayNumber
    }

    fun toggleDayCompletion(planId: String, dayNumber: Int) {
        viewModelScope.launch {
            readingPlanRepository.toggleDayCompletion(planId, dayNumber)
        }
    }

    fun resetPlan(planId: String) {
        viewModelScope.launch {
            readingPlanRepository.resetProgress(planId)
            _selectedDay.value = 1
        }
    }

    class Factory(
        private val readingPlanRepository: ReadingPlanRepository,
        private val settingsRepository: SettingsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReadingPlanViewModel::class.java)) {
                return ReadingPlanViewModel(readingPlanRepository, settingsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
