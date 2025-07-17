// Ride Hailing Widget - iOS and Android only
// Created by Umer
@file:OptIn(ExperimentalMaterial3Api::class)

package org.umer.cmpplayground

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Mock data for demonstration
private val mockRideData = """
{
  "success": true,
  "timestamp": "2024-01-15T10:30:00Z",
  "location": {
    "latitude": 40.7128,
    "longitude": -74.0060,
    "address": "New York, NY"
  },
  "available_rides": [
    {
      "id": "ride_001",
      "driver_name": "Sarah Johnson",
      "vehicle_type": "sedan",
      "vehicle_model": "Toyota Camry",
      "license_plate": "ABC-123",
      "rating": 4.8,
      "total_trips": 1247,
      "eta_minutes": 3,
      "distance_km": 0.8,
      "fare_estimate": {
        "base_fare": 5.50,
        "distance_fare": 2.30,
        "time_fare": 1.20,
        "total": 9.00,
        "currency": "USD"
      },
      "vehicle_color": "Silver"
    },
    {
      "id": "ride_002", 
      "driver_name": "Michael Chen",
      "vehicle_type": "suv",
      "vehicle_model": "Honda CR-V",
      "license_plate": "XYZ-789",
      "rating": 4.9,
      "total_trips": 892,
      "eta_minutes": 7,
      "distance_km": 1.2,
      "fare_estimate": {
        "base_fare": 6.00,
        "distance_fare": 3.50,
        "time_fare": 1.80,
        "total": 11.30,
        "currency": "USD"
      },
      "vehicle_color": "Black"
    }
  ],
  "surge_pricing": {
    "active": false,
    "multiplier": 1.0,
    "reason": null
  },
  "estimated_wait_time": "3-12 minutes"
}
"""

// Simple data classes for the widget
data class MockRideInfo(
    val id: String,
    val driverName: String,
    val vehicleType: String,
    val vehicleModel: String,
    val rating: Double,
    val etaMinutes: Int,
    val fareTotal: Double,
    val vehicleColor: String
)

@Composable
fun RideHailingWidget() {
    // Platform check - only show on iOS and Android
    if (!isIosOrAndroid()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Ride Hailing is only available on iOS and Android",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    // Mock rides data
    val mockRides = listOf(
        MockRideInfo(
            id = "ride_001",
            driverName = "Sarah Johnson",
            vehicleType = "sedan",
            vehicleModel = "Toyota Camry",
            rating = 4.8,
            etaMinutes = 3,
            fareTotal = 9.00,
            vehicleColor = "Silver"
        ),
        MockRideInfo(
            id = "ride_002",
            driverName = "Michael Chen",
            vehicleType = "suv",
            vehicleModel = "Honda CR-V",
            rating = 4.9,
            etaMinutes = 7,
            fareTotal = 11.30,
            vehicleColor = "Black"
        ),
        MockRideInfo(
            id = "ride_003",
            driverName = "Emily Rodriguez",
            vehicleType = "premium",
            vehicleModel = "BMW 3 Series",
            rating = 4.7,
            etaMinutes = 5,
            fareTotal = 14.30,
            vehicleColor = "White"
        )
    )

    var loading by remember { mutableStateOf(false) }
    var selectedRide by remember { mutableStateOf<MockRideInfo?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "🚗 Available Rides",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            
            // Refresh button
            IconButton(
                onClick = { 
                    loading = true
                    // Simulate API call
                    loading = false
                }
            ) {
                Text("🔄", fontSize = 20.sp)
            }
        }

        // Location info
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📍", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        "Current Location",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "New York, NY",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Rides list
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mockRides) { ride ->
                    RideCard(
                        ride = ride,
                        onBookRide = { selectedRide = it }
                    )
                }
            }
        }

        // Booking confirmation
        selectedRide?.let { ride ->
            BookingDialog(
                ride = ride,
                onDismiss = { selectedRide = null },
                onConfirm = { 
                    selectedRide = null
                    // Handle booking
                }
            )
        }
    }
}

@Composable
private fun RideCard(
    ride: MockRideInfo,
    onBookRide: (MockRideInfo) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Driver info and vehicle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        ride.driverName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${ride.vehicleModel} • ${ride.vehicleColor}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⭐", fontSize = 16.sp)
                    Text(
                        "${ride.rating}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ETA and Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ETA
                Column {
                    Text(
                        "ETA",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${ride.etaMinutes} min",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Price
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        "Fare",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "$${String.format("%.2f", ride.fareTotal)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Book button
                Button(
                    onClick = { onBookRide(ride) },
                    modifier = Modifier.padding(start = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Book")
                }
            }
        }
    }
}

@Composable
private fun BookingDialog(
    ride: MockRideInfo,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Confirm Booking")
        },
        text = {
            Column {
                Text("Driver: ${ride.driverName}")
                Text("Vehicle: ${ride.vehicleModel}")
                Text("ETA: ${ride.etaMinutes} minutes")
                Text("Fare: $${String.format("%.2f", ride.fareTotal)}")
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

// Platform check function
@Composable
private fun isIosOrAndroid(): Boolean {
    // This is a simplified check - in real implementation you'd use expect/actual
    return true // For demo purposes, allowing all platforms
    // In real app, you'd use platform-specific code like:
    // return Platform.isIOS || Platform.isAndroid
} 