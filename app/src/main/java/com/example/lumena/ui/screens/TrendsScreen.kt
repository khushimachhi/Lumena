package com.example.lumena.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lumena.data.mood.MoodRepository
import com.example.lumena.viewmodel.TrendsViewModel
import com.example.lumena.viewmodel.TrendsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen(
    moodRepo: MoodRepository,
    onBack: () -> Unit
) {
    val viewModel: TrendsViewModel = viewModel(
        factory = TrendsViewModelFactory(moodRepo)
    )

    val days by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Weekly Trends") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            items(days) { day ->
                TrendRow(day.date, day.mood, day.energy)
                Divider()
            }
        }
    }
}

@Composable
fun TrendRow(
    day: String,
    mood: Int?,
    energy: Int?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(day, style = MaterialTheme.typography.bodyLarge)

        Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
            Text(
                text = "Mood: ${mood?.toString() ?: "-"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Energy: ${energy?.toString() ?: "-"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
