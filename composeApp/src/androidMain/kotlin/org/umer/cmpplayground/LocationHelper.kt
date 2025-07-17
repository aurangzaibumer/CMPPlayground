// Android Location Helper
// Created by Umer
package org.umer.cmpplayground

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidLocationHelper(private val context: Context) {
    
    suspend fun getCurrentLocation(): LocationData {
        return suspendCancellableCoroutine { continuation ->
            try {
                // Check if location permission is granted
                val fineLocationPermission = ContextCompat.checkSelfPermission(
                    context, 
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
                val coarseLocationPermission = ContextCompat.checkSelfPermission(
                    context, 
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                
                if (fineLocationPermission != PackageManager.PERMISSION_GRANTED && 
                    coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                    // No permission, return default location
                    continuation.resume(LocationData(40.7128, -74.0060, cityName = "New York"))
                    return@suspendCancellableCoroutine
                }
                
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                
                // Get last known location
                val providers = locationManager.getProviders(true)
                var bestLocation: Location? = null
                
                for (provider in providers) {
                    try {
                        val location = locationManager.getLastKnownLocation(provider)
                        if (location != null && (bestLocation == null || location.accuracy < bestLocation.accuracy)) {
                            bestLocation = location
                        }
                    } catch (e: SecurityException) {
                        // Permission denied, continue to next provider
                    }
                }
                
                if (bestLocation != null) {
                    continuation.resume(
                        LocationData(
                            latitude = bestLocation.latitude,
                            longitude = bestLocation.longitude,
                            accuracy = bestLocation.accuracy,
                            cityName = "Current Location"
                        )
                    )
                } else {
                    // No location found, return default
                    continuation.resume(LocationData(40.7128, -74.0060, cityName = "New York"))
                }
                
            } catch (e: Exception) {
                // Error getting location, return default
                continuation.resume(LocationData(40.7128, -74.0060, cityName = "New York"))
            }
        }
    }
} 