package com.example.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun WeatherBackground(conditionText: String, isDay: Boolean, content: @Composable () -> Unit) {
    val t = conditionText.lowercase()

    val colors = when {
        "sun" in t && isDay -> listOf(Color(0xFFFFE08A), Color(0xFFFFB347))
        "partly" in t || ("cloud" in t && isDay) -> listOf(Color(0xFFd9e4f5), Color(0xFFb3c6e6))
        "cloud" in t || "overcast" in t -> listOf(Color(0xFF7f8fa6), Color(0xFF596275))
        "rain" in t || "drizzle" in t -> listOf(Color(0xFFa1c4fd), Color(0xFFc2e9fb))
        "snow" in t -> listOf(Color(0xFFe0eafc), Color(0xFFcfdef3))
        else -> if (isDay) listOf(Color(0xFFdfe9f3), Color(0xFFffffff))
        else listOf(Color(0xFF0f2027), Color(0xFF203a43))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors))
    ) {
        content()
    }
}
