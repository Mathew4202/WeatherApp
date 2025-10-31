package com.example.weatherapp.services

import retrofit2.http.GET
import retrofit2.http.Query

// -------- Retrofit interface --------
interface WeatherService {
    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String,
        @Query("alerts") alerts: String
    ): ApiResponse
}

// -------- DTOs that match WeatherAPI JSON --------
data class ApiResponse(
    val location: LocationDto,
    val current: CurrentDto,
    val forecast: ForecastDto
)

data class LocationDto(
    val name: String,
    val region: String,
    val country: String,
    val tz_id: String,
    val localtime: String
)

data class CurrentDto(
    val temp_c: Double,
    val wind_kph: Double,
    val wind_dir: String,
    val precip_mm: Double,
    val humidity: Int,
    val condition: ConditionDto
)

data class ForecastDto(
    val forecastday: List<ForecastDayDto>
)

data class ForecastDayDto(
    val date: String,
    val day: DayDto
)

data class DayDto(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val totalprecip_mm: Double,
    val maxwind_kph: Double,
    val avghumidity: Int,
    val daily_chance_of_rain: Int,
    val daily_chance_of_snow: Int,
    val condition: ConditionDto
)

data class ConditionDto(
    val text: String,
    val icon: String,
    val code: Int
)
