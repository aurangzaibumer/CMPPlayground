package org.umer.cmpplayground

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import cmpplayground.composeapp.generated.resources.Res
import cmpplayground.composeapp.generated.resources.compose_multiplatform
import org.umer.cmpplayground.ReminderApp
import org.umer.cmpplayground.WeatherInspirationScreen

// Main App entry point
// Created by Umer
@ExperimentalMaterial3Api
@Composable
@Preview
fun App() {
    var showWeather by remember { mutableStateOf(false) }
    MaterialTheme {
        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { showWeather = false }, modifier = Modifier.weight(1f)) { Text("Reminders") }
                Button(onClick = { showWeather = true }, modifier = Modifier.weight(1f)) { Text("Weather + Inspiration") }
            }
            Divider()
            if (showWeather) {
                WeatherInspirationScreen()
            } else {
                ReminderApp()
            }
        }
    }
}