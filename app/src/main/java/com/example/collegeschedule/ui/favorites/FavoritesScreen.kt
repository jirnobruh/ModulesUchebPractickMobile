package com.example.collegeschedule.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.store.FavoritesStore
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.repository.ScheduleRepository
import com.example.collegeschedule.ui.components.GroupChip
import com.example.collegeschedule.utils.getWeekDateRange
import com.example.collegeschedule.ui.schedule.ScheduleList
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(repository: ScheduleRepository, favoritesStore: FavoritesStore) {

    val favorites by favoritesStore.favoritesFlow.collectAsState(initial = emptySet())

    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().padding(12.dp)) {

        Text("Избранные группы", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(12.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            favorites.forEach { group ->
                GroupChip(
                    group = group,
                    selected = selectedGroup == group,
                    onClick = {
                        selectedGroup = group
                        loading = true
                        val (start, end) = getWeekDateRange()

                        scope.launch {
                            try {
                                schedule = repository.loadSchedule(group, start, end)
                            } catch (e: Exception) {
                                error = e.message
                            } finally {
                                loading = false
                            }
                        }
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            loading -> CircularProgressIndicator()
            error != null -> Text("Ошибка: $error")
            schedule.isNotEmpty() -> ScheduleList(schedule)
        }
    }
}
