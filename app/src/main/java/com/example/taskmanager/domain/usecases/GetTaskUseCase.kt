package com.example.taskmanager.domain.usecases

import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository

class GetTaskUseCase(private val taskManagerRepository: TaskManagerRepository) {

    fun getTask(id: Int): Task {
        return taskManagerRepository.getTask(id)
    }
}