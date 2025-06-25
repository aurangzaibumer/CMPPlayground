// Weather and Daily Inspiration data models
// Created by Umer
package org.umer.cmpplayground

import kotlinx.serialization.Serializable

// Open-Meteo API response (simplified)
@Serializable
data class WeatherResponse(
    val current_weather: CurrentWeather? = null
)

@Serializable
data class CurrentWeather(
    val temperature: Double? = null,
    val weathercode: Int? = null
)

// ZenQuotes API response
@Serializable
data class InspirationQuote(
    val q: String, // quote
    val a: String  // author
) 