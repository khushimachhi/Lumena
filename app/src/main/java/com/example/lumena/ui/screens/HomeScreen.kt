package com.example.lumena.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumena.data.mood.MoodRepository
import com.example.lumena.data.onboarding.OnboardingRepository
import com.example.lumena.viewmodel.HomeViewModel
import com.example.lumena.viewmodel.HomeViewModelFactory

@Composable
fun HomeScreen(
    onboardingRepo: OnboardingRepository,
    moodRepo: MoodRepository,
    onCheckInClick: () -> Unit,
    onToolsClick: () -> Unit,
    onTrendsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(onboardingRepo, moodRepo)
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshTodayMood()
    }

    if (uiState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Hi, ${uiState.nickname}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(6.dp))

        val subtitle = when (uiState.persona) {
            "Professional" -> "Hope your workday went well!"
            "Student" -> "How's your day going?"
            "Parent" -> "Taking care of yourself matters too."
            else -> "How are you feeling today?"
        }

        Text(subtitle, style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(24.dp))

        // Today's mood card
        if (uiState.todayMood != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Today's Check-In", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))

                    Text("Mood: ${uiState.todayMood}")
                    Text("Energy: ${uiState.todayEnergy}")
                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = onCheckInClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Check-In")
                    }
                }
            }
        } else {
            Button(
                onClick = onCheckInClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log Today’s Mood")
            }
        }

        Spacer(Modifier.height(30.dp))

        // Quick action buttons
        Button(
            onClick = onToolsClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Try a Breathing Exercise") }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onTrendsClick,
            modifier = Modifier.fillMaxWidth()
        ) { Text("View Last 7 Days") }

        Spacer(Modifier.height(20.dp))

        TextButton(
            onClick = { onAboutClick() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("About & Safety")
        }

    }
}
