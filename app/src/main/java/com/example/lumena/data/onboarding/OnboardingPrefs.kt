package com.example.lumena.data.onboarding

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Create DataStore instance
private val Context.dataStore by preferencesDataStore(name = "onboarding_prefs")

class OnboardingPrefs(private val context: Context) {

    companion object {
        private val KEY_NICKNAME = stringPreferencesKey("nickname")
        private val KEY_AGE_BRACKET = stringPreferencesKey("ageBracket")
        private val KEY_PERSONA = stringPreferencesKey("persona")
        private val KEY_COMPLETED = booleanPreferencesKey("onboardingCompleted")
    }

    // Save onboarding data
    suspend fun saveOnboardingData(
        nickname: String,
        ageBracket: String,
        persona: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NICKNAME] = nickname
            prefs[KEY_AGE_BRACKET] = ageBracket
            prefs[KEY_PERSONA] = persona
            prefs[KEY_COMPLETED] = true
        }
    }

    // Flows for reading
    val onboardingCompleted: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_COMPLETED] ?: false
        }

    val nickname: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_NICKNAME] ?: ""
        }

    val ageBracket: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_AGE_BRACKET] ?: ""
        }

    val persona: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_PERSONA] ?: "Professional"
        }
}
