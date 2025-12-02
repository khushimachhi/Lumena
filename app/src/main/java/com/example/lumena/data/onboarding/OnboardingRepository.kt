package com.example.lumena.data.onboarding

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    suspend fun saveProfile(nickname: String, ageBracket: String, persona: String)
    fun onboardingCompleted(): Flow<Boolean>
    fun getNickname(): Flow<String>
    fun getPersona(): Flow<String>
}

class OnboardingRepositoryImpl(
    private val prefs: OnboardingPrefs
) : OnboardingRepository {

    override suspend fun saveProfile(
        nickname: String,
        ageBracket: String,
        persona: String
    ) {
        prefs.saveOnboardingData(nickname, ageBracket, persona)
    }

    override fun onboardingCompleted(): Flow<Boolean> = prefs.onboardingCompleted

    override fun getNickname(): Flow<String> = prefs.nickname

    override fun getPersona(): Flow<String> = prefs.persona
}
