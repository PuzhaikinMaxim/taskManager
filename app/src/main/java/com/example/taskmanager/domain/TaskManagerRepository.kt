package com.example.taskmanager.domain

import androidx.lifecycle.LiveData
import java.util.*

interface TaskManagerRepository {

    fun getTasksList() : LiveData<List<Task>>

    fun getTasksList(start: GregorianCalendar,
                     end: GregorianCalendar) : LiveData<List<Task>>

    fun getTasksList(date: GregorianCalendar) : LiveData<List<Task>>

    fun getTaskDatesList(start: GregorianCalendar,
                         end: GregorianCalendar): LiveData<List<GregorianCalendar>>

    fun getOutdatedTasks(): LiveData<List<Task>>

    fun getTask(id: Int): Task

    fun addTask(task: Task)

    fun editTask(task: Task)

    fun removeTask(id: Int)
}