package com.example.lumena.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumena.data.mood.MoodRepository
import com.example.lumena.data.onboarding.OnboardingRepository
import com.example.lumena.ui.components.BentoSection
import com.example.lumena.ui.components.WeeklyDateBar
import com.example.lumena.viewmodel.HomeViewModel
import com.example.lumena.viewmodel.HomeViewModelFactory
import java.time.LocalDate

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

    LaunchedEffect(Unit) { viewModel.refreshTodayMood() }

    if (uiState.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9EDC9))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {

        // GREETING
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFF9C74F))
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Hello, ${uiState.nickname}",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(Modifier.height(20.dp))

        // WEEKLY BAR
        var selectedDate by remember { mutableStateOf(LocalDate.now()) }

        WeeklyDateBar(
            selectedDate = selectedDate,
            onDaySelected = { selectedDate = it }
        )

        Spacer(Modifier.height(26.dp))

        // SUBTITLE
        val subtitle = when (uiState.persona) {
            "Professional" -> "Hope your workday went well!"
            "Student" -> "How's your day going?"
            "Parent" -> "Taking care of yourself matters too."
            else -> "How are you feeling today?"
        }

        Text(
            subtitle,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 4.dp)
        )

        Spacer(Modifier.height(20.dp))

        // BENTO GRID
        BentoSection(
            onCheckInClick = onCheckInClick,
            onToolsClick = onToolsClick,
            onTrendsClick = onTrendsClick
        )

        Spacer(Modifier.height(32.dp))

        // FOOTER BUTTONS (OPTIONAL)
        TextButton(
            onClick = onAboutClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("About & Safety")
        }
    }
}
