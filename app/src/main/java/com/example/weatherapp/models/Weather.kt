package com.example.weatherapp.models

// Main wrapper model: current weather + forecast list
data class Weather(
    val current: Current,
    val forecast: List<Forecast>
)

// Model for the current weather screen
data class Current(
    val image: Int,
    val condition: String,
    val temperature: Int,
    val precipitation: String,
    val wind: String
)

// Model for the forecast screen
data class Forecast(
    val image: Int,
    val date: String,
    val condition: String,
    val high: Int,
    val low: Int,
    val precipitation: String,
    val wind: String,
    val humidity: Int
)


