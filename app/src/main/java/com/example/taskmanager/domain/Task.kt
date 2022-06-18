package com.example.taskmanager.domain

import java.util.*

data class Task(
    var taskName: String,
    var taskDescription: String,
    var taskDate: GregorianCalendar,
    var priorityType: PriorityTypes,
    var isOutdated: Boolean= NOT_OUTDATED,
    var isDone: Boolean = NOT_DONE,
    var id: Int = UNDEFINED_ID
    ){

    companion object {
        const val UNDEFINED_ID = -1
        const val NOT_OUTDATED = false
        const val NOT_DONE = false
    }
}
