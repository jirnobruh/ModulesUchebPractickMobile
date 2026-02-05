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
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {

                            // Заголовок пары
                            Text(
                                text = "Пара ${lesson.lessonNumber}  •  ${lesson.time}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )

                            val full = lesson.groupParts[LessonGroupPart.FULL]
                            val sub1 = lesson.groupParts[LessonGroupPart.SUB1]
                            val sub2 = lesson.groupParts[LessonGroupPart.SUB2]

                            if (full != null) {
                                // Общая пара
                                LessonInfoBlock(
                                    subject = full.subject,
                                    teacher = full.teacher,
                                    building = full.building,
                                    classroom = full.classroom
                                )
                            } else {
                                // Две подгруппы
                                Row(Modifier.fillMaxWidth()) {

                                    Column(Modifier.weight(1f)) {
                                        if (sub1 != null) {
                                            Text(
                                                "1 подгруппа",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            LessonInfoBlock(
                                                subject = sub1.subject,
                                                teacher = sub1.teacher,
                                                building = sub1.building,
                                                classroom = sub1.classroom
                                            )
                                        }
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Column(Modifier.weight(1f)) {
                                        if (sub2 != null) {
                                            Text(
                                                "2 подгруппа",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            LessonInfoBlock(
                                                subject = sub2.subject,
                                                teacher = sub2.teacher,
                                                building = sub2.building,
                                                classroom = sub2.classroom
                                            )
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
@Composable
fun LessonInfoBlock(
    subject: String,
    teacher: String,
    building: String,
    classroom: String
) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(subject, style = MaterialTheme.typography.bodyLarge)
        Text(teacher, style = MaterialTheme.typography.bodyMedium)
        Text(
            "$building, $classroom",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
