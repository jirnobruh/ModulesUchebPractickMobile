package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance
import com.example.collegeschedule.utils.getWeekDateRange

@Composable
fun ScheduleScreen() {
    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Загружаем список групп
    LaunchedEffect(Unit) {
        try {
            groups = RetrofitInstance.api.getGroups()
        } catch (e: Exception) {
            error = "Ошибка загрузки групп: ${e.message}"
        }
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {

        // Поле поиска + Dropdown
        Box {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    expanded = true
                },
                label = { Text("Выберите группу") },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                groups
                    .filter { it.contains(searchQuery, ignoreCase = true) }
                    .forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group) },
                            onClick = {
                                selectedGroup = group
                                searchQuery = group
                                expanded = false
                            }
                        )
                    }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Кнопка загрузки расписания
        Button(
            onClick = {
                if (selectedGroup != null) {
                    loading = true
                    error = null
                    val (start, end) = getWeekDateRange()

                    LaunchedEffect(selectedGroup) {
                        try {
                            schedule = RetrofitInstance.api.getSchedule(
                                selectedGroup!!,
                                start,
                                end
                            )
                        } catch (e: Exception) {
                            error = "Ошибка загрузки расписания: ${e.message}"
                        } finally {
                            loading = false
                        }
                    }
                }
            },
            enabled = selectedGroup != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Показать расписание")
        }

        Spacer(Modifier.height(16.dp))

        when {
            loading -> CircularProgressIndicator()
            error != null -> Text(error!!)
            schedule.isNotEmpty() -> ScheduleList(schedule)
        }
    }
}