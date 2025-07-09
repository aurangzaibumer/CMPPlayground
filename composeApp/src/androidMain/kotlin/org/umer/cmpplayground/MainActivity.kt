@file:OptIn(ExperimentalMaterial3Api::class)

package org.umer.cmpplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {
            Scaffold {
                App(it)
            }

        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
fun AppAndroidPreview() {
    App()
}