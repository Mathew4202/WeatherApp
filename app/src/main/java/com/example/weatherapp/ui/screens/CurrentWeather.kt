package com.example.weatherapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentWeatherScreen(vm: MainViewModel) {
    val current = vm.weather.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Current Weather") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painterResource(id = current.image),
                contentDescription = current.condition,
                modifier = Modifier.size(100.dp)
            )

            Text("Condition: ${current.condition}")
            Text("Temperature: ${current.temperature}°C")
            Text("Precipitation: ${current.precipitation}")
            Text("Wind: ${current.wind}")
        }
    }
}
