package com.example.lumena.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lumena.data.onboarding.OnboardingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val nickname: String = "",
    val ageBracket: String = "",
    val persona: String = "",
    val consentGiven: Boolean = false,
    val isSaving: Boolean = false,
    val completed: Boolean = false
)

class OnboardingViewModel(
    private val repo: OnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    fun onNicknameChange(value: String) {
        _uiState.value = _uiState.value.copy(nickname = value)
    }

    fun onAgeBracketChange(value: String) {
        _uiState.value = _uiState.value.copy(ageBracket = value)
    }

    fun onPersonaChange(value: String) {
        _uiState.value = _uiState.value.copy(persona = value)
    }

    fun onConsentToggle(value: Boolean) {
        _uiState.value = _uiState.value.copy(consentGiven = value)
    }

    fun saveOnboarding() {
        val state = _uiState.value
        if (state.nickname.isBlank() || state.ageBracket.isBlank() || state.persona.isBlank() || !state.consentGiven) {
            return // invalid → UI should block button
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)

            repo.saveProfile(
                nickname = state.nickname,
                ageBracket = state.ageBracket,
                persona = state.persona
            )

            _uiState.value = _uiState.value.copy(
                isSaving = false,
                completed = true
            )
        }
    }
}


class OnboardingViewModelFactory(
    private val repo: OnboardingRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) ->
                OnboardingViewModel(repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}