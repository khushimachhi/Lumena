package com.example.lumena.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lumena.data.mood.MoodEntry
import com.example.lumena.data.mood.MoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class CheckInUiState(
    val mood: Int = 0,          // 1-5
    val energy: Float = 5f,     // 0–10 slider
    val note: String = "",
    val loading: Boolean = true,
    val saved: Boolean = false
)

class CheckInViewModel(
    private val repo: MoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState

    private val todayIso = LocalDate.now().toString()

    init {
        loadToday()
    }

    private fun loadToday() {
        viewModelScope.launch {
            val entry = repo.getEntryForDate(todayIso)
            if (entry != null) {
                _uiState.value = CheckInUiState(
                    mood = entry.mood,
                    energy = entry.energy.toFloat(),
                    note = entry.note ?: "",
                    loading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(loading = false)
            }
        }
    }

    fun onMoodChange(value: Int) {
        _uiState.value = _uiState.value.copy(mood = value)
    }

    fun onEnergyChange(value: Float) {
        _uiState.value = _uiState.value.copy(energy = value)
    }

    fun onNoteChange(value: String) {
        _uiState.value = _uiState.value.copy(note = value)
    }

    fun saveEntry() {
        val state = _uiState.value
        if (state.mood == 0) return   // require mood selected

        viewModelScope.launch {
            repo.saveOrUpdate(
                MoodEntry(
                    dateIso = todayIso,
                    mood = state.mood,
                    energy = state.energy.toInt(),
                    note = state.note
                )
            )
            _uiState.value = _uiState.value.copy(saved = true)
        }
    }
}

class CheckInViewModelFactory(
    private val repo: MoodRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CheckInViewModel(repo) as T
    }
}
