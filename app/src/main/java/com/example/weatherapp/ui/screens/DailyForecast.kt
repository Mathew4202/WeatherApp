package com.example.weatherapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.models.Forecast

@Composable
fun DailyForecastScreen(vm: MainViewModel) {
    val forecast = vm.weather?.forecast

    // soft gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE8F1FF), Color(0xFFF8FBFF))
                )
            )
            .padding(12.dp)
    ) {
        when {
            forecast == null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            forecast.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No forecast available")
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(forecast) { day ->
                        ForecastRow(day, vm)
                    }
                }
            }
        }
    }
}

@Composable
private fun ForecastRow(item: Forecast, vm: MainViewModel) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.iconUrl,
                contentDescription = item.condition,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Fit
            )

            Column(Modifier.weight(1f)) {
                Text(item.date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(item.condition, style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(6.dp))

                // Temps always exist in your model (Int), just format them:
                Text(
                    "High ${vm.formatTempCtoCurrent(item.high)} • Low ${vm.formatTempCtoCurrent(item.low)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                // 🔽 Handle nullable wind/humidity safely with nice fallbacks
                val windPretty = item.maxWindKph
                    ?.let { vm.formatWindKphToCurrent(it.toInt()) }
                    ?: item.wind // fall back to the preformatted string you already store

                val humidityPretty = item.humidity
                    ?.toInt()
                    ?.let { "$it%" }
                    ?: "--%"

                Text(
                    "🌬 $windPretty   💧 $humidityPretty   ☔ ${item.precipitation}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
