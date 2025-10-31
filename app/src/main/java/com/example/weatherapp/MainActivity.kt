package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.screens.CurrentWeatherScreen
import com.example.weatherapp.ui.screens.DailyForecastScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.google.accompanist.permissions.isGranted

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                DisplayUI(mainViewModel)
            }
        }
    }
}

@OptIn(com.google.accompanist.permissions.ExperimentalPermissionsApi::class)
@Composable
fun GetLocation() {
    val permissionState =
        com.google.accompanist.permissions.rememberPermissionState(
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

    if (permissionState.status.isGranted) {
        android.util.Log.i("TESTING", "Hurray, permission granted!")

        // Get Location
        val currentContext = androidx.compose.ui.platform.LocalContext.current
        val fusedLocationClient =
            com.google.android.gms.location.LocationServices
                .getFusedLocationProviderClient(currentContext)

        if (androidx.core.content.ContextCompat.checkSelfPermission(
                currentContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            val cancellationTokenSource =
                com.google.android.gms.tasks.CancellationTokenSource()

            android.util.Log.i("TESTING", "Requesting location...")

            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude.toString()
                    val lng = location.longitude.toString()

                    android.util.Log.i("TESTING", "Success: $lat $lng")

                    val coordinates = "$lat,$lng"
                } else {
                    android.util.Log.i("TESTING", "Problem encountered: Location returned null")
                }
            }
        }
    } else {
        // Run a side-effect (coroutine) to get permission. The permission popup.
        androidx.compose.runtime.LaunchedEffect(permissionState) {
            permissionState.launchPermissionRequest()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayUI(vm: MainViewModel) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: "home"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val loc = vm.weather?.location?.let { "${it.name}, ${it.region}" } ?: "Loading…"
                    Text(loc)
                },
                actions = {
                    TextButton(onClick = { vm.toggleUnits() }) {
                        Text(if (vm.useMetric) "°C" else "°F")
                    }
                    TextButton(onClick = { vm.load() }) {
                        Text("Refresh")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") },
                    icon = { Icon(painterResource(id = android.R.drawable.ic_menu_myplaces), contentDescription = null) },
                    label = { Text("Current") }
                )
                NavigationBarItem(
                    selected = currentRoute == "forecast",
                    onClick = { navController.navigate("forecast") },
                    icon = { Icon(painterResource(id = android.R.drawable.ic_menu_week), contentDescription = null) },
                    label = { Text("Daily Forecast") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                Column {
                    // 🔹 Calls your teacher’s exact GetLocation() composable
                    GetLocation()
                    // 🔹 Your existing UI
                    CurrentWeatherScreen(vm)
                }
            }
            composable("forecast") {
                DailyForecastScreen(vm)
            }
        }
    }
}
