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

data class DayTrend(
    val date: String,
    val mood: Int?,
    val energy: Int?
)

class TrendsViewModel(
    private val repo: MoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<DayTrend>>(emptyList())
    val uiState: StateFlow<List<DayTrend>> = _uiState

    init {
        loadLast7Days()
    }

    private fun loadLast7Days() {
        viewModelScope.launch {

            val today = LocalDate.now()
            val last7 = (0..6).map { today.minusDays(it.toLong()) }.sorted()

            val start = last7.first().toString()
            val end = last7.last().toString()

            repo.getEntriesBetween(start, end).collect { entries ->

                val mapped = last7.map { date ->
                    val entry = entries.find { it.dateIso == date.toString() }
                    DayTrend(
                        date = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() },
                        mood = entry?.mood,
                        energy = entry?.energy
                    )
                }

                _uiState.value = mapped
            }
        }
    }
}

class TrendsViewModelFactory(
    private val repo: MoodRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrendsViewModel(repo) as T
    }
}
