package com.example.taskmanager.data

data class BasicStatistics(
    val amountOfAllTasks: Int,
    val amountOfCompletedTasks: Int,
    val amountOfTasksWithHighPriority: Int,
    val amountOfTasksWithMediumPriority: Int,
    val amountOfTasksWithLowPriority: Int,
    val amountOfOutdatedTasks: Int
)
