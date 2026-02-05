package com.example.collegeschedule.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.store.FavoritesStore
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.repository.ScheduleRepository
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

        LazyColumn {
            items(favorites.toList()) { group ->
                Text(
                    group,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedGroup = group
                            loading = true
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
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge
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
