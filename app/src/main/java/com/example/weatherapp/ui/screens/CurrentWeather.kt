package com.example.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.ui.components.WeatherBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(vm: MainViewModel) {
    val w = vm.weather
    if (w == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    WeatherBackground(conditionText = w.current.condition, isDay = true) { // isDay optional
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.15f) // subtle overlay
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
            ) {
                // Real WeatherAPI icon
                AsyncImage(
                    model = w.current.iconUrl,
                    contentDescription = w.current.condition,
                    modifier = Modifier.size(96.dp)
                )

                Text(text = w.current.condition, style = MaterialTheme.typography.titleMedium)
                Text(text = vm.formatTempCtoCurrent(w.current.temperature), style = MaterialTheme.typography.displaySmall)
                Text(text = "Precip: ${w.current.precipitation}")
                Text(text = "Wind: ${vm.formatWindKphToCurrent(w.current.windKph?.toInt() ?: 0)}")
            }
        }
    }
}
