package com.example.lumena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MoodPicker(
    selected: Int,
    onSelect: (Int) -> Unit
) {
    println("MoodPicker ACTIVE — selected=$selected")

    val moods = listOf("😞", "😕", "😐", "🙂", "😄")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        moods.forEachIndexed { index, emoji ->
            val moodValue = index + 1

            Text(
                text = emoji,
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        println("Mood Picker CLICKED → $moodValue")
                        onSelect(moodValue)
                    },
                style =
                if (selected == moodValue)
                    MaterialTheme.typography.headlineLarge
                else
                    MaterialTheme.typography.headlineMedium
            )
        }
    }
}
