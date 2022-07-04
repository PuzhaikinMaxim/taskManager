package com.example.taskmanager.domain

data class Statistics(
    val amountOfAllTasks: Int,
    val amountOfCompletedTasks: Int,
    val amountOfTasksWithHighPriority: Int,
    val amountOfTasksWithMediumPriority: Int,
    val amountOfTasksWithLowPriority: Int,
    val amountOfOutdatedTasks: Int,
    val amountOfTodayTasks: Int,
    val amountOfTodayCompletedTasks: Int
)
