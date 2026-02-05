package com.example.collegeschedule.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


fun getWeekDateRange(): Pair<String, String> {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ISO_DATE
    // Если сегодня воскресенье — стартуем с понедельника
    var start = if (today.dayOfWeek == DayOfWeek.SUNDAY)
        today.plusDays(1)
    else
        today
    var daysAdded = 0
    var end = start
    // Нужно получить ровно 6 учебных дней (ПН–СБ)
    while (daysAdded < 5) {
        end = end.plusDays(1)
        if (end.dayOfWeek != DayOfWeek.SUNDAY) {
            daysAdded++
        }
    }
    return start.format(formatter) to end.format(formatter)
}

fun formatScheduleDate(dateString: String, weekday: String): String {
    val date = LocalDate.parse(dateString.substring(0, 10)) // "2026-02-05"
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val formattedDate = date.format(formatter)

    val weekdayLower = weekday.lowercase(Locale("ru"))

    return "$formattedDate – $weekdayLower"
}