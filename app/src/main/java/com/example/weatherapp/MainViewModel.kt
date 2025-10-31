package com.example.weatherapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Forecast
import com.example.weatherapp.models.Location
import com.example.weatherapp.models.Weather
import com.example.weatherapp.services.*
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    // --- OkHttp with logging so we can see problems in Logcat ---
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(WeatherService::class.java)

    // Observable state (null = loading)
    var weather by mutableStateOf<Weather?>(null)
        private set

    // --- UNIT TOGGLE (C/F) ---
    var useMetric by mutableStateOf(true)
        private set
    fun toggleUnits() { useMetric = !useMetric }

    // Helpers to present values with the current unit choice
    fun formatTempCtoCurrent(tempC: Int): String =
        if (useMetric) "$tempC°C" else "${(tempC * 9 / 5) + 32}°F"

    fun formatWindKphToCurrent(kph: Int): String =
        if (useMetric) "$kph km/h" else "${(kph * 0.621371).toInt()} mph"


    fun load(cityQuery: String = "Halifax") {
        val apiKey = ApiKeys.WEATHER_API_KEY
        viewModelScope.launch {
            try {
                val resp = api.getForecast(
                    key = apiKey,
                    q = cityQuery,
                    days = 14,
                    aqi = "no",
                    alerts = "no"
                )
                weather = mapToAppModels(resp)
                Log.d("WeatherApp", "Loaded: current=${resp.current.condition.text}, days=${resp.forecast.forecastday.size}")
            } catch (e: Exception) {
                Log.e("WeatherApp", "Failed to load/parse forecast", e)
                // Fallback so the spinner stops and you see something
                weather = Weather(
                    location = Location("Halifax", "Nova Scotia", "Canada"),
                    current = Current(
                        image = R.drawable.clowdy,
                        condition = "Overcast",
                        temperature = 12,
                        precipitation = "0mm",
                        wind = "SW 15 km/h"
                    ),
                    forecast = emptyList()
                )
            }
        }
    }

    fun loadByCoords(lat: Double, lon: Double, days: Int = 14) {
        val apiKey = ApiKeys.WEATHER_API_KEY
        val coords = "$lat,$lon" // API accepts "lat,lon"

        viewModelScope.launch {
            try {
                val resp = api.getForecast(
                    key = apiKey,
                    q = coords,
                    days = days,
                    aqi = "no",
                    alerts = "no"
                )
                weather = mapToAppModels(resp)
                Log.d("WeatherApp", "Loaded by coords: $lat,$lon")
            } catch (e: Exception) {
                Log.e("WeatherApp", "Failed to load forecast by coords", e)
            }
        }
    }

    private fun mapToAppModels(r: ApiResponse): Weather {
        val loc = Location(
            name = r.location.name,
            region = r.location.region,
            country = r.location.country,
            tzId = r.location.tz_id,
            localtime = r.location.localtime
        )

        val current = Current(
            image = iconFor(r.current.condition.text),
            condition = r.current.condition.text,
            temperature = r.current.temp_c.toInt(),
            precipitation = "${r.current.precip_mm}mm",
            wind = "${r.current.wind_dir} ${r.current.wind_kph.toInt()} km/h",

            iconUrl = ensureHttps(r.current.condition.icon),
            tempC = r.current.temp_c,
            feelsLikeC = r.current.temp_c,
            windKph = r.current.wind_kph,
            windDir = r.current.wind_dir,
            precipMm = r.current.precip_mm,
            humidityPct = r.current.humidity
        )

        val days = r.forecast.forecastday.map { d ->
            val chance = maxOf(d.day.daily_chance_of_rain, d.day.daily_chance_of_snow)
            val type = when {
                d.day.daily_chance_of_snow > d.day.daily_chance_of_rain -> "Snow"
                d.day.daily_chance_of_rain > 0 -> "Rain"
                else -> "None"
            }
            Forecast(
                image = 0,
                date = d.date,
                condition = d.day.condition.text,
                high = d.day.maxtemp_c.toInt(),
                low = d.day.mintemp_c.toInt(),
                precipitation = "${d.day.totalprecip_mm}mm, ${chance}%",
                wind = "${d.day.maxwind_kph.toInt()} km/h",
                humidity = d.day.avghumidity,

                iconUrl = ensureHttps(d.day.condition.icon),
                precipAmountMm = d.day.totalprecip_mm,
                precipProbability = chance,
                precipType = type,
                maxWindKph = d.day.maxwind_kph
            )
        }

        return Weather(location = loc, current = current, forecast = days)
    }

    private fun ensureHttps(icon: String) =
        if (icon.startsWith("//")) "https:$icon" else icon

    private fun iconFor(text: String): Int {
        val t = text.lowercase()
        return when {
            "sun" in t && "cloud" !in t -> R.drawable.sunny
            "partly" in t -> R.drawable.partlysunny
            "rain" in t || "drizzle" in t || "shower" in t -> R.drawable.rainy
            "snow" in t -> R.drawable.snowy
            "cloud" in t || "overcast" in t -> R.drawable.clowdy
            else -> R.drawable.clowdy
        }
    }

    init { load("Halifax") }
}
