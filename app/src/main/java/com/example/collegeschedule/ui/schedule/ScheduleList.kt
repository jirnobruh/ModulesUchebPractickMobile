package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.LessonGroupPart
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.utils.formatScheduleDate

@Composable
fun ScheduleList(data: List<ScheduleByDateDto>) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(data) { day ->
            val formattedDate = formatScheduleDate(day.lessonDate, day.weekday)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Divider(thickness = 1.dp)

                Text(
                    formattedDate,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )

                Divider(thickness = 1.dp)
            }


            if (day.lessons.isEmpty()) {
                Text(
                    "Нет занятий",
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            } else {
                day.lessons.forEach { lesson ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text("Пара ${lesson.lessonNumber} (${lesson.time})")

                            val full = lesson.groupParts[LessonGroupPart.FULL]
                            val sub1 = lesson.groupParts[LessonGroupPart.SUB1]
                            val sub2 = lesson.groupParts[LessonGroupPart.SUB2]

                            if (full != null) {
                                // Общая пара
                                Text("${full.subject}")
                                Text(full.teacher)
                                Text("${full.building}, ${full.classroom}")
                            } else {
                                // Таблица 1/2 подгруппы
                                Row(Modifier.fillMaxWidth()) {
                                    Column(Modifier.weight(1f)) {
                                        if (sub1 != null) {
                                            Text("1 подгр: ${sub1.subject}")
                                            Text(sub1.teacher)
                                            Text("${sub1.building}, ${sub1.classroom}")
                                        }
                                    }
                                    Column(Modifier.weight(1f)) {
                                        if (sub2 != null) {
                                            Text("2 подгр: ${sub2.subject}")
                                            Text(sub2.teacher)
                                            Text("${sub2.building}, ${sub2.classroom}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}