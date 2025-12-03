package com.example.lumena.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size


@Composable
fun MoodSelectorRow(
    selectedMood: Int,
    onMoodSelected: (Int) -> Unit
) {
    Log.d("MoodSelectorRow", "Recomposed — selected = $selectedMood")

    val emojis = listOf("😞", "😕", "😐", "🙂", "😄")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        emojis.forEachIndexed { index, emoji ->
            val moodValue = index + 1

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        Log.d("MoodSelectorRow", "Clicked mood = $moodValue")
                        onMoodSelected(moodValue)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emoji,
                    style = if (selectedMood == moodValue)
                        MaterialTheme.typography.headlineLarge
                    else
                        MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
