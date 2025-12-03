package com.example.lumena.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumena.viewmodel.OnboardingViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.ui.platform.LocalContext

// DataStore repo imports
import com.example.lumena.data.onboarding.OnboardingPrefs
import com.example.lumena.data.onboarding.OnboardingRepositoryImpl
import com.example.lumena.viewmodel.OnboardingViewModelFactory

// Opt-in for experimental FlowRow API
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.example.lumena.data.onboarding.OnboardingRepository

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    repo: OnboardingRepository,
    onContinue: () -> Unit
) {
    // create prefs & repo only once per composition
    val context = LocalContext.current
    val prefs = remember { OnboardingPrefs(context) }
    val repo = remember { OnboardingRepositoryImpl(prefs) }
    val factory = remember { OnboardingViewModelFactory(repo) }

    // obtain the ViewModel with the factory so it can receive the repo in its constructor
    val viewModel: OnboardingViewModel = viewModel(factory = OnboardingViewModelFactory(repo))

    val uiState by viewModel.uiState.collectAsState()

    // When completed, trigger navigation
    if (uiState.completed) {
        LaunchedEffect(Unit) {
            onContinue()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Welcome to Lumena",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Lumena is a simple wellness companion that helps you reflect on your day.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nickname
        OutlinedTextField(
            value = uiState.nickname,
            onValueChange = viewModel::onNicknameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nickname") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Age bracket selector
        Text("Select your age group", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        val ageOptions = listOf("13–17", "18–24", "25–39", "40–59", "60+")
        Row(Modifier.horizontalScroll(rememberScrollState())) {
            ageOptions.forEach { option ->
                FilterChip(
                    selected = uiState.ageBracket == option,
                    onClick = { viewModel.onAgeBracketChange(option) },
                    label = { Text(option) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Persona selector
        Text("Who describes you best?", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        val personaOptions = listOf("Professional", "Student", "Parent", "Senior", "Other")
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3
        ) {
            personaOptions.forEach { p ->
                FilterChip(
                    selected = uiState.persona == p,
                    onClick = { viewModel.onPersonaChange(p) },
                    label = { Text(p) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Consent checkbox
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = uiState.consentGiven,
                    onValueChange = viewModel::onConsentToggle
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = uiState.consentGiven,
                onCheckedChange = viewModel::onConsentToggle
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "I understand Lumena is not a medical or crisis service.",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        val isFormValid =
            uiState.nickname.isNotBlank() &&
                    uiState.ageBracket.isNotBlank() &&
                    uiState.persona.isNotBlank() &&
                    uiState.consentGiven

        Button(
            onClick = { viewModel.saveOnboarding() },
            enabled = isFormValid && !uiState.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isSaving) "Saving..." else "Continue")
        }
    }
}
