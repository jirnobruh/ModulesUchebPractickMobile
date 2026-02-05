package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.repository.ScheduleRepository
import com.example.collegeschedule.data.store.FavoritesStore
import com.example.collegeschedule.ui.components.GroupSelector
import com.example.collegeschedule.utils.getWeekDateRange
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(repository: ScheduleRepository, favoritesStore: FavoritesStore) {

    var groups by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val favorites by favoritesStore.favoritesFlow.collectAsState(initial = emptySet())
    val scope = rememberCoroutineScope()

    // Загружаем список групп
    LaunchedEffect(Unit) {
        try {
            groups = repository.loadGroups()
        } catch (e: Exception) {
            error = "Ошибка загрузки групп: ${e.message}"
        }
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {

        // Поле поиска + Dropdown
        GroupSelector(
            groups = groups,
            selectedGroup = selectedGroup,
            onGroupSelected = { selectedGroup = it }
        )

        Spacer(Modifier.height(12.dp))

        // Кнопка избранного
        if (selectedGroup != null) {
            val isFavorite = favorites.contains(selectedGroup)

            Button(
                onClick = {
                    scope.launch {
                        if (isFavorite)
                            favoritesStore.removeFavorite(selectedGroup!!)
                        else
                            favoritesStore.addFavorite(selectedGroup!!)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isFavorite) "Удалить из избранного" else "Добавить в избранное")
            }

            Spacer(Modifier.height(12.dp))
        }


        // Кнопка загрузки расписания
        Button(
            onClick = {
                if (selectedGroup != null) {
                    loading = true
                    error = null
                    val (start, end) = getWeekDateRange()

                    scope.launch {
                        try {
                            schedule = repository.loadSchedule(
                                selectedGroup!!,
                                start,
                                end
                            )
                        } catch (e: Exception) {
                            error = e.message
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