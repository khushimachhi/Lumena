package com.example.lumena.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumena.data.mood.MoodRepository
import com.example.lumena.ui.components.MoodPicker
import com.example.lumena.viewmodel.CheckInViewModel
import com.example.lumena.viewmodel.CheckInViewModelFactory

@Composable
fun CheckInScreen(
    viewModel: CheckInViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.saved) {
        LaunchedEffect(Unit) { onBack() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("How was your day?", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        MoodPicker(
            selected = uiState.mood,
            onSelect = { viewModel.onMoodChange(it) }
        )



        Spacer(Modifier.height(16.dp))

        // Energy slider
        Text("Energy: ${uiState.energy.toInt()}")
        Slider(
            value = uiState.energy,
            onValueChange = { viewModel.onEnergyChange(it) },
            valueRange = 0f..10f
        )

        Spacer(Modifier.height(16.dp))

        // Optional note
        OutlinedTextField(
            value = uiState.note,
            onValueChange = { viewModel.onNoteChange(it) },
            label = { Text("Add a note (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // Save button
        Button(
            onClick = { viewModel.saveEntry() },
            enabled = uiState.mood != 0,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

        Spacer(Modifier.height(16.dp))

        Text("DEBUG: mood = ${uiState.mood}")
    }
}

