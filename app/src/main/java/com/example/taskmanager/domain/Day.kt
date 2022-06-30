package com.example.taskmanager.domain

data class Day(
    val dayOfMonth: Int,
    val isEveryTaskDone: Boolean = false,
    val isEmpty: Boolean = true
) {
}