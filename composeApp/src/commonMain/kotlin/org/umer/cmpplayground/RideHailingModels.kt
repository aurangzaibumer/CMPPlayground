// Ride Hailing data models
// Created by Umer
package org.umer.cmpplayground

import kotlinx.serialization.Serializable

// JSONBin.io response wrapper
@Serializable
data class JsonBinResponse(
    val record: RideHailingResponse
)

@Serializable
data class RideHailingResponse(
    val success: Boolean,
    val timestamp: String,
    val location: UserLocation,
    val available_rides: List<RideInfo>,
    val surge_pricing: SurgePricing,
    val estimated_wait_time: String
)

@Serializable
data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

@Serializable
data class RideInfo(
    val id: String,
    val driver_name: String,
    val vehicle_type: String,
    val vehicle_model: String,
    val license_plate: String,
    val rating: Double,
    val total_trips: Int,
    val eta_minutes: Int,
    val distance_km: Double,
    val fare_estimate: FareEstimate,
    val driver_location: DriverLocation,
    val vehicle_color: String,
    val phone_number: String
)

@Serializable
data class FareEstimate(
    val base_fare: Double,
    val distance_fare: Double,
    val time_fare: Double,
    val total: Double,
    val currency: String
)

@Serializable
data class DriverLocation(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class SurgePricing(
    val active: Boolean,
    val multiplier: Double,
    val reason: String?
) 