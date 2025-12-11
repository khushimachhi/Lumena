package com.example.lumena.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun WeeklyDateBar(
    selectedDate: LocalDate,
    onDaySelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val startOfWeek = today.minusDays(today.dayOfWeek.ordinal.toLong())
    val daysOfWeek = (0..4).map { startOfWeek.plusDays(it.toLong()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEach { date ->
            val isSelected = date == selectedDate
            val isToday = date == today

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onDaySelected(date) }
            ) {
                Text(
                    text = date.dayOfWeek.name.take(3),
                    style = MaterialTheme.typography.labelMedium,
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        else -> Color.Gray
                    }
                )

                Spacer(Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                else -> Color.LightGray.copy(alpha = 0.2f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = when {
                            isSelected -> Color.White
                            else -> Color.Black
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
