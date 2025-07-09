package org.umer.cmpplayground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

// Main App entry point
@ExperimentalMaterial3Api
@Composable
@Preview
fun App(contentPadding : PaddingValues = PaddingValues()) {
    var showWeather by remember { mutableStateOf(false) }
    MaterialTheme {
        Column(Modifier.fillMaxSize().padding(contentPadding)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { showWeather = false }, modifier = Modifier.weight(1f)) { Text("Reminders") }
                Button(onClick = { showWeather = true }, modifier = Modifier.weight(1f)) { Text("Weather + Inspiration") }
            }
            HorizontalDivider()
            if (showWeather) {
                WeatherInspirationScreen()
            } else {
                ReminderApp()
            }
        }
    }
}