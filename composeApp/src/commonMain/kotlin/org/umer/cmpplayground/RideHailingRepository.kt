// Ride Hailing repository
// Created by Umer
package org.umer.cmpplayground

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class RideHailingRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // Your jsonbin.io API URL
    private val apiUrl = "https://api.jsonbin.io/v3/qs/687907cfdb4fa954e67c5f98"

    suspend fun fetchAvailableRides(latitude: Double, longitude: Double): RideHailingResponse? {
        return try {
            // JSONBin.io returns data wrapped in a 'record' field
            val response: JsonBinResponse = client.get(apiUrl).body()
            response.record
        } catch (e: Exception) {
            println("Error fetching rides: ${e.message}")
            null
        }
    }

    suspend fun bookRide(rideId: String): Boolean {
        return try {
            // Mock booking - in real app this would be a POST request
            client.post("${apiUrl}/book") {
                parameter("rideId", rideId)
            }
            true
        } catch (e: Exception) {
            false
        }
    }
} 