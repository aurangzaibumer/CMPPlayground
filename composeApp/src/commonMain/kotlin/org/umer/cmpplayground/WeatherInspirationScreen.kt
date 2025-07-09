// Weather + Daily Inspiration UI
// Created by Umer
@file:OptIn(ExperimentalMaterial3Api::class)

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
    val locationService = remember { LocationService() }
    
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var quote by remember { mutableStateOf<InspirationQuote?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val locations = locationService.getPopularLocations()

    fun refresh() {
        selectedLocation?.let { location ->
            loading = true
            error = null
            scope.launch {
                try {
                    weather = repo.fetchWeather(location.latitude, location.longitude)
                    quote = repo.fetchInspiration()
                } catch (e: Exception) {
                    error = "Failed to load data: ${e.message}"
                } finally {
                    loading = false
                }
            }
        }
    }

    // Auto-select first location and load data on start
    LaunchedEffect(Unit) {
        selectedLocation = locations.first()
        refresh()
    }

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
        
        // Location Selector with Refresh Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedLocation?.cityName ?: "Select City",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Location") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    locations.forEach { location ->
                        DropdownMenuItem(
                            text = { Text("${location.cityName} (${location.latitude}, ${location.longitude})") },
                            onClick = {
                                selectedLocation = location
                                expanded = false
                                refresh()
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Refresh Icon Button
            IconButton(
                onClick = { refresh() },
                enabled = !loading && selectedLocation != null,
                modifier = Modifier.size(56.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (loading) MaterialTheme.colorScheme.surfaceVariant 
                                       else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = "🔄",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
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
                selectedLocation?.let { location ->
                    Text(
                        "📍 ${location.cityName}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
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