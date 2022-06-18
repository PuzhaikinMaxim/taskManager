package com.example.taskmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.taskmanager.domain.PriorityTypes
import com.example.taskmanager.domain.Task
import java.util.*

@Entity(tableName = "tasks")
data class TaskTable(
    val taskName: String,
    val taskDescription: String,
    @field:TypeConverters(GregorianCalendarConverter::class)
    val taskDate: GregorianCalendar,
    val priorityType: PriorityTypes,
    val isOutdated: Boolean,
    val isDone: Boolean,
    @PrimaryKey val id: Int
) {
}