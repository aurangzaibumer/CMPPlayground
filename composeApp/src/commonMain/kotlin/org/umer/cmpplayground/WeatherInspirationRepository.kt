// Weather and Daily Inspiration repository
// Created by Umer
package org.umer.cmpplayground

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class WeatherInspirationRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // Fetch weather for a city (using Open-Meteo, needs lat/lon)
    suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse? {
        val url = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current_weather=true"
        return try {
            client.get(url).body()
        } catch (e: Exception) {
            null
        }
    }

    // Fetch daily inspiration quote (ZenQuotes)
    suspend fun fetchInspiration(): InspirationQuote? {
        val url = "https://zenquotes.io/api/random"
        return try {
            val response: List<InspirationQuote> = client.get(url).body()
            response.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
} 