package com.example.taskmanager.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.TaskManagerRepositoryImpl
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.usecases.*

class TaskListViewModel : ViewModel() {

    private val repository = TaskManagerRepositoryImpl

    private val removeTaskUseCase = RemoveTaskUseCase(repository)
    private val editTaskUseCase = EditTaskUseCase(repository)
    private val getTasksList = GetTasksListUseCase(repository)

    private val tasksList = getTasksList.getTasksList()

    fun removeTask(task: Task){
        removeTaskUseCase.removeTask(task.id)
    }

    fun changeReadyState(task: Task) {
        val newTask = task.copy(isDone = !task.isDone)
        editTaskUseCase.editTask(task)
    }
}