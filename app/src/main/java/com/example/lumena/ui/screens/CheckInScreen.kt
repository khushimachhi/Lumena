package com.example.lumena.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumena.data.mood.MoodRepository
import com.example.lumena.ui.components.MoodSelectorRow
import com.example.lumena.viewmodel.CheckInViewModel
import com.example.lumena.viewmodel.CheckInViewModelFactory

@Composable
fun CheckInScreen(
    moodRepo: MoodRepository,
    onBack: () -> Unit
) {
    val viewModel: CheckInViewModel = viewModel(
        factory = CheckInViewModelFactory(moodRepo)
    )

    val uiState by viewModel.uiState.collectAsState()


    if (uiState.saved) {
        LaunchedEffect(Unit) { onBack() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text("How was your day?", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(20.dp))

        MoodSelectorRow(
            selectedMood = uiState.mood,
            onMoodSelected = { viewModel.onMoodChange(it) }
        )





        Spacer(Modifier.height(20.dp))

        Text("Energy: ${uiState.energy.toInt()}")
        Slider(
            value = uiState.energy,
            onValueChange = { viewModel.onEnergyChange(it) },
            valueRange = 0f..10f
        )
        Text("Selected mood = ${uiState.mood}")
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            label = { Text("Add a note (optional)") },
            value = uiState.note,
            onValueChange = viewModel::onNoteChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = { viewModel.saveEntry() },
            enabled = uiState.mood != 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}
