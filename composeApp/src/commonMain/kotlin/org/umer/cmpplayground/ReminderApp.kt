// ReminderApp UI for Reminder App
// Created by Umer

package org.umer.cmpplayground

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

@ExperimentalMaterial3Api
@Composable
fun ReminderApp() {
    val repository = remember { ReminderRepository() }
    var reminders by remember { mutableStateOf(repository.getAll()) }
    var showDialog by remember { mutableStateOf(false) }
    var editReminder by remember { mutableStateOf<Reminder?>(null) }

    fun refresh() { reminders = repository.getAll() }

    Scaffold(
        bottomBar = {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("VentureDive", style = MaterialTheme.typography.bodySmall)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editReminder = null
                showDialog = true
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (reminders.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No reminders yet.")
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(reminders) { reminder ->
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    editReminder = reminder
                                    showDialog = true
                                }
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(reminder.title, style = MaterialTheme.typography.titleMedium)
                                    Text(reminder.description, style = MaterialTheme.typography.bodyMedium)
                                }
                                Text(
                                    "Delete",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .clickable {
                                            repository.delete(reminder.id)
                                            refresh()
                                        }
                                )
                            }
                        }
                    }
                }
            }
            if (showDialog) {
                ReminderDialog(
                    initial = editReminder,
                    onDismiss = { showDialog = false },
                    onSave = { title, desc ->
                        if (editReminder == null) {
                            repository.add(Reminder(title = title, description = desc))
                        } else {
                            repository.update(editReminder!!.copy(title = title, description = desc))
                        }
                        refresh()
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ReminderDialog(
    initial: Reminder?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue(initial?.title ?: "")) }
    var desc by remember { mutableStateOf(TextFieldValue(initial?.description ?: "")) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initial == null) "Add Reminder" else "Edit Reminder") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    singleLine = false
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.text.isNotBlank()) {
                        onSave(title.text, desc.text)
                    }
                }
            ) { Text("Save") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
} 