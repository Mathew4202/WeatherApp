package com.example.weatherapp

import androidx.lifecycle.ViewModel
import com.example.weatherapp.models.*

class MainViewModel : ViewModel() {

    // Weather data stored in the ViewModel
    val weather: Weather

    init {
        // Populate with placeholder data
        weather = Weather(
            current = Current(
                image = R.drawable.partlysunny,
                condition = "Partly Cloudy",
                temperature = 18,
                precipitation = "Light rain, 2mm",
                wind = "SW at 15 km/h"
            ),
            forecast = listOf(
                Forecast(
                    image = R.drawable.sunny,
                    date = "Today",
                    condition = "Sunny",
                    high = 20,
                    low = 14,
                    precipitation = "0mm, 0%",
                    wind = "SW 10 km/h",
                    humidity = 45
                ),
                Forecast(
                    image = R.drawable.rainy,
                    date = "Tue",
                    condition = "Rainy",
                    high = 18,
                    low = 12,
                    precipitation = "4mm, 60%",
                    wind = "E 20 km/h",
                    humidity = 70
                ),
                Forecast(
                    image = R.drawable.clowdy,
                    date = "Wed",
                    condition = "Cloudy",
                    high = 16,
                    low = 11,
                    precipitation = "1mm, 20%",
                    wind = "N 15 km/h",
                    humidity = 55
                ),
                Forecast(
                    image = R.drawable.partlysnowy,
                    date = "Thu",
                    condition = "Partly Snowy",
                    high = 6,
                    low = 4,
                    precipitation = "1mm, 40%",
                    wind = "N 55 km/h",
                    humidity = 18
                ),
                Forecast(
                    image = R.drawable.snowy,
                    date = "Fri",
                    condition = "Snowy",
                    high = 3,
                    low = -5,
                    precipitation = "10mm, 60%",
                    wind = "N 80 km/h",
                    humidity = 5
                )
            )
        )
    }
}



