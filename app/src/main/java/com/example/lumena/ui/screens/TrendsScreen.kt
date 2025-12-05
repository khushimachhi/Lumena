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
import com.example.lumena.data.mood.MoodEntry
import com.example.lumena.data.mood.MoodRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Vico imports
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen(
    moodRepo: MoodRepository,
    onBack: () -> Unit
) {
    var entries by remember { mutableStateOf<List<MoodEntry>>(emptyList()) }

    // Load data
    LaunchedEffect(Unit) {
        entries = moodRepo.getLastEntries(7)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Last 7 Check-ins") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            if (entries.isEmpty()) {
                Text("No recent records found.")
                return@Column
            }

            // Recent entries
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(entries) { entry ->
                    TrendsItem(entry)
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Chart title
            Text(
                text = "Mood Trend",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))


            // 🚀 Build chart model
            val chartEntries = entries
                .sortedBy { it.dateIso }
                .mapIndexed { index, entry ->
                    entryOf(index.toFloat(), entry.mood.toFloat())
                }

            val chartModelProducer = remember(chartEntries) {
                ChartEntryModelProducer(chartEntries)
            }


            // Chart
            Chart(
                chart = lineChart(),
                chartModelProducer = chartModelProducer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        }
    }
}


@Composable
fun TrendsItem(entry: MoodEntry) {
    val emoji = listOf("😞", "😕", "😐", "🙂", "😄")[entry.mood - 1]

    val formatter = DateTimeFormatter.ofPattern("dd MMM, EEE")
    val formatted = LocalDate.parse(entry.dateIso).format(formatter)

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(formatted, style = MaterialTheme.typography.bodyLarge)
        Text("Mood: $emoji    Energy: ${entry.energy}")
    }
}
