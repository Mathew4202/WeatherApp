package com.example.weatherapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.models.Forecast

@Composable
fun DailyForecastScreen(vm: MainViewModel) {
    val forecastList = vm.weather.forecast

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(forecastList) { item: Forecast ->
            ForecastRow(item)
        }
    }
}

@Composable
fun ForecastRow(item: Forecast) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = item.date, style = MaterialTheme.typography.titleMedium)
                Text(text = "Condition: ${item.condition}")
                Text(text = "High: ${item.high}°C, Low: ${item.low}°C")
                Text(text = "Precipitation: ${item.precipitation}")
                Text(text = "Wind: ${item.wind}")
                Text(text = "Humidity: ${item.humidity}%")
            }
            Image(
                painter = painterResource(id = item.image),
                contentDescription = item.condition,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}
