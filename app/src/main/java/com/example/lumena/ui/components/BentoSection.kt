package com.example.lumena.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BentoSection(
    onCheckInClick: () -> Unit,
    onToolsClick: () -> Unit,
    onTrendsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // LEFT COLUMN
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BentoCard(
                title = "Check-in",
                icon = Icons.Default.Face,
                color = Color(0xFFF6BD60),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                onClick = onCheckInClick
            )

            BentoCard(
                title = "Trends",
                icon = Icons.Default.ShowChart,
                color = Color(0xFF84A59D),
                modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
                onClick = onTrendsClick
            )
        }

        // RIGHT COLUMN
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BentoCard(
                title = "Breathing",
                icon = Icons.Default.Eco,
                color = Color(0xFFF5CAC3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                onClick = onToolsClick
            )

            BentoCard(
                title = "Safety",
                icon = Icons.Default.HealthAndSafety,
                color = Color(0xFFF28482),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                onClick = {}
            )
        }
    }
}
