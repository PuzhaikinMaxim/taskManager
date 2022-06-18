package com.example.taskmanager.domain.usecases

import com.example.taskmanager.domain.TaskManagerRepository

class RemoveTaskUseCase(private val taskManagerRepository: TaskManagerRepository) {

    fun removeTask(id: Int){
        taskManagerRepository.removeTask(id)
    }
}