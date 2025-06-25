// Reminder data model for Reminder App
// Created by Umer
package org.umer.cmpplayground

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Reminder(
    val id: String = Random.nextInt(1, Int.MAX_VALUE).toString(),
    val title: String,
    val description: String
)