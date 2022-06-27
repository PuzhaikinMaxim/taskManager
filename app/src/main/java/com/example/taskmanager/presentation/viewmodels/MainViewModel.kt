package com.example.taskmanager.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.TaskManagerRepositoryImpl
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.usecases.EditTaskUseCase
import com.example.taskmanager.domain.usecases.GetTasksListByDayUseCase
import com.example.taskmanager.domain.usecases.GetTasksListUseCase
import com.example.taskmanager.domain.usecases.RemoveTaskUseCase
import java.util.*

class MainViewModel : ViewModel() {

    private val repository = TaskManagerRepositoryImpl

    private val getTasksListByDayUseCase = GetTasksListByDayUseCase(repository)
    private val removeTaskUseCase = RemoveTaskUseCase(repository)
    private val editTaskUseCase = EditTaskUseCase(repository)
    private val getTasksListUseCase = GetTasksListUseCase(repository)

    val tasks = getTasksListUseCase.getTasksList()

    fun removeTask(task: Task){
        removeTaskUseCase.removeTask(task.id)
    }

    fun changeReadyState(task: Task) {
        val newTask = task.copy(isDone = !task.isDone)
        editTaskUseCase.editTask(task)
    }

    init {
        val gregorianCalendar = GregorianCalendar(
            2022,
            5,
            27
        )
        println(gregorianCalendar.toString())
        getTasksListByDayUseCase.getTasksListByDay(gregorianCalendar)
    }
}