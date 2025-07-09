package org.umer.cmpplayground

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float? = null,
    val cityName: String? = null
)

class LocationService {
    // For now, we'll use some popular cities as examples
    private val popularLocations = listOf(
        LocationData(40.7128, -74.0060, cityName = "New York"),
        LocationData(34.0522, -118.2437, cityName = "Los Angeles"),
        LocationData(51.5074, -0.1278, cityName = "London"),
        LocationData(35.6762, 139.6503, cityName = "Tokyo"),
        LocationData(48.8566, 2.3522, cityName = "Paris"),
        LocationData(55.7558, 37.6173, cityName = "Moscow"),
        LocationData(37.7749, -122.4194, cityName = "San Francisco"),
        LocationData(52.5200, 13.4050, cityName = "Berlin"),
        LocationData(41.9028, 12.4964, cityName = "Rome"),
        LocationData(25.2048, 55.2708, cityName = "Dubai")
    )
    
    fun getPopularLocations(): List<LocationData> = popularLocations
    
    suspend fun getCurrentLocation(): LocationData {
        // For now, return a default location (New York)
        // This can be enhanced with actual GPS/network location services
        return popularLocations.first()
    }
    
    fun isLocationEnabled(): Boolean = true
    
    suspend fun requestLocationPermission(): Boolean = true
} 