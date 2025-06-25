// Weather + Daily Inspiration UI
// Created by Umer
package org.umer.cmpplayground

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun WeatherInspirationScreen() {
    val repo = remember { WeatherInspirationRepository() }
    var lat by remember { mutableStateOf(40.7128) } // Default: New York
    var lon by remember { mutableStateOf(-74.0060) }
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var quote by remember { mutableStateOf<InspirationQuote?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun refresh() {
        loading = true
        error = null
        scope.launch {
            try {
                weather = repo.fetchWeather(lat, lon)
                quote = repo.fetchInspiration()
            } catch (e: Exception) {
                error = "Failed to load data."
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) { refresh() }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Weather & Daily Inspiration",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 24.sp)
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = lat.toString(),
                onValueChange = { it.toDoubleOrNull()?.let { v -> lat = v } },
                label = { Text("Latitude") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = lon.toString(),
                onValueChange = { it.toDoubleOrNull()?.let { v -> lon = v } },
                label = { Text("Longitude") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { refresh() }, enabled = !loading) {
            Text(if (loading) "Loading..." else "Refresh")
        }
        Spacer(Modifier.height(24.dp))
        if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(16.dp))
        }
        // Weather Section
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Current Weather", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                weather?.current_weather?.let {
                    Text("Temperature: ${it.temperature ?: "-"}°C", fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Weather Code: ${it.weathercode ?: "-"}", fontSize = 14.sp, color = Color.Gray)
                } ?: Text("No weather data.", color = Color.Gray)
            }
        }
        Spacer(Modifier.height(24.dp))
        // Inspiration Section
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Daily Inspiration", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                quote?.let {
                    Text("\"${it.q}\"", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(8.dp))
                    Text("- ${it.a}", fontSize = 14.sp, color = Color.Gray)
                } ?: Text("No inspiration yet.", color = Color.Gray)
            }
        }
    }
} 