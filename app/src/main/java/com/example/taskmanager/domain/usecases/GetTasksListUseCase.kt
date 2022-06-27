package com.example.taskmanager.domain.usecases

import androidx.lifecycle.LiveData
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository

class GetTasksListUseCase(private val taskManagerRepository: TaskManagerRepository) {

    fun getTasksList(): LiveData<List<Task>>{
        return taskManagerRepository.getTasksList()
    }
}