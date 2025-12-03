package com.example.lumena.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lumena.data.mood.MoodRepository
import com.example.lumena.data.onboarding.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate

data class HomeUiState(
    val nickname: String = "",
    val persona: String = "",
    val todayMood: Int? = null,
    val todayEnergy: Int? = null,
    val loading: Boolean = true
)

class HomeViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val moodRepo: MoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val todayIso = LocalDate.now().toString()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            combine(
                onboardingRepo.getNickname(),
                onboardingRepo.getPersona()
            ) { nickname, persona ->
                nickname to persona
            }.collect { (nickname, persona) ->

                val entry = moodRepo.getEntryForDate(todayIso)

                _uiState.value = HomeUiState(
                    nickname = nickname,
                    persona = persona,
                    todayMood = entry?.mood,
                    todayEnergy = entry?.energy,
                    loading = false
                )
            }
        }
    }
}

class HomeViewModelFactory(
    private val onboardingRepo: OnboardingRepository,
    private val moodRepo: MoodRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(onboardingRepo, moodRepo) as T
    }
}
