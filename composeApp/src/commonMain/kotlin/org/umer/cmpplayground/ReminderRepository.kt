// ReminderRepository for Reminder App
// Created by Umer
package org.umer.cmpplayground

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.russhwolf.settings.*
import kotlinx.serialization.Serializable
import kotlin.random.Random

class ReminderRepository {
    private val settings = Settings()
    private val key = "reminders_json"
    private var reminders = loadReminders().toMutableList()

    private fun loadReminders(): List<Reminder> {
        val json = settings.getString(key, "")
        return if (json.isNotEmpty()) {
            try {
                Json.decodeFromString<List<Reminder>>(json)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    private fun saveReminders() {
        settings.putString(key, Json.encodeToString<List<Reminder>>(reminders))
    }

    fun getAll(): List<Reminder> = reminders.toList()

    fun add(reminder: Reminder) {
        reminders.add(reminder)
        saveReminders()
    }

    fun update(reminder: Reminder) {
        val index = reminders.indexOfFirst { it.id == reminder.id }
        if (index != -1) {
            reminders[index] = reminder
            saveReminders()
        }
    }

    fun delete(reminderId: String) {
        reminders.removeAll { it.id == reminderId }
        saveReminders()
    }

    fun getById(reminderId: String): Reminder? = reminders.find { it.id == reminderId }
}