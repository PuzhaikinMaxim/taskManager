package com.example.taskmanager.domain

import java.util.*

data class Task(
    val id: Int,
    val taskName: String,
    val taskDescription: String,
    val taskDate: GregorianCalendar,
    val priorityType: PriorityTypes,
    val isDone: Boolean = false
    )
