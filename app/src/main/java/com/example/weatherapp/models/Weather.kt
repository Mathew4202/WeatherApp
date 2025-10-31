package com.example.weatherapp.models

// New: Location model from WeatherAPI "location"
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val tzId: String? = null,
    val localtime: String? = null
)

data class Weather(
    val location: Location,
    val current: Current,
    val forecast: List<Forecast>
)

data class Current(
    val image: Int,
    val condition: String,
    val temperature: Int,
    val precipitation: String,
    val wind: String,

    // ---- JSON fields from WeatherAPI "current" ----
    val iconUrl: String? = null,
    val tempC: Double? = null,
    val feelsLikeC: Double? = null,
    val windKph: Double? = null,
    val windDir: String? = null,
    val precipMm: Double? = null,
    val humidityPct: Int? = null
)

// Forecast per day
data class Forecast(
    val image: Int,
    val date: String,
    val condition: String,
    val high: Int,
    val low: Int,
    val precipitation: String,
    val wind: String,
    val humidity: Int,

    // ----JSON fields from "forecast.forecastday[].day" ----
    val iconUrl: String? = null,
    val precipAmountMm: Double? = null,
    val precipProbability: Int? = null,
    val precipType: String? = null,
    val maxWindKph: Double? = null
)
