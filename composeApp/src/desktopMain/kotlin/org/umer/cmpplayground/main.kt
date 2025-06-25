package org.umer.cmpplayground

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@ExperimentalMaterial3Api
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CMP Playground",
    ) {
        App()
    }
}