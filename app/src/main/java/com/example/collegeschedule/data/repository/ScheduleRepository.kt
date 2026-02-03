package com.example.collegeschedule.data.repository

import com.example.collegeschedule.data.api.ScheduleApi
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.ApiProvider


class ScheduleRepository {

    suspend fun loadSchedule(group: String, start: String, end: String): List<ScheduleByDateDto> {
        return ApiProvider.request { api ->
            api.getSchedule(group, start, end)
        }
    }

    suspend fun loadGroups(): List<String> {
        return ApiProvider.request { api ->
            api.getGroups()
        }
    }
}
/*class ScheduleRepository(private val api: ScheduleApi) {
    suspend fun loadSchedule(group: String): List<ScheduleByDateDto> {
        return api.getSchedule(
            groupName = group,
            start = "2026-01-12",
            end = "2026-01-17"
        )
    }
    suspend fun loadGroups(): List<String> {
        return api.getGroups()
    }
}*/