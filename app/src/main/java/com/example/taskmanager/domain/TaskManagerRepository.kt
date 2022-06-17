package com.example.taskmanager.domain

import androidx.lifecycle.LiveData
import java.util.*

interface TaskManagerRepository {

    fun getTasksList(gregorianCalendar: GregorianCalendar) : LiveData<List<Task>>

    fun getTask(): LiveData<Task>

    fun addTask(task: Task)

    fun editTask(task: Task)

    fun removeTask(task: Task)
}