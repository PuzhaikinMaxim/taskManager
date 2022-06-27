package com.example.taskmanager.domain.usecases

import androidx.lifecycle.LiveData
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository

class GetOutdatedTasksUseCase(private val taskManagerRepository: TaskManagerRepository) {

    fun getOutdatedTasks(): LiveData<List<Task>> {
        return taskManagerRepository.getOutdatedTasks()
    }
}